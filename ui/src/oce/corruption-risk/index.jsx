import React from 'react';
import cn from 'classnames';
import URI from 'urijs';
import { Map } from 'immutable';
import PropTypes from 'prop-types';
import { defaultMemoize } from 'reselect';
import { isEqualWith } from 'lodash';
import { withRouter } from 'react-router-dom';
import {
  cacheFn, debounce, fetchJson, range,
} from '../tools';
import OverviewPage from './overview-page';
import CorruptionTypePage from './corruption-type';
import IndividualIndicatorPage from './individual-indicator';
import ContractsPage from './contracts';
import ContractPage from './contracts/single';
import SuppliersPage from './suppliers';
import SupplierPage from './suppliers/single';
import ProcuringEntitiesPage from './procuring-entities';
import BuyersPage from './buyers';
import ProcuringEntityPage from './procuring-entities/single';
import BuyerPage from './buyers/single';
import Filters from './filters';
import LandingPopup from './landing-popup';
import { LOGIN_URL } from './constants';
import './style.scss';
import Sidebar from './sidebar';
import makueniLogo from '../resources/makueni-logo.png';
import { LOCALES } from '../translatable';

const getIndicators = cacheFn((indicatorTypesMapping, corruptionType) => Object.keys(indicatorTypesMapping)
  .filter((key) => indicatorTypesMapping[key].types.indexOf(corruptionType) > -1));

/**
 * Returns true if two objects are equal shallowly.
 */
const isShallowEqual = (a, b) => isEqualWith(a, b,
  (aval, bval, index) => (index === undefined ? undefined : Object.is(aval, bval)));

/**
 * Creates a selector that will return filters without date properties.
 * Changes to date properties won't result in a new filters object.
 */
const createDatelessFiltersSelector = (excludeProps) => {
  const memoizeDatelessFilters = defaultMemoize((filters) => (filters), isShallowEqual);
  return (filters) => {
    const datelessFilters = { ...filters };
    delete datelessFilters.year;
    delete datelessFilters.month;
    if (excludeProps) {
      excludeProps.forEach((prop) => delete datelessFilters[prop]);
    }
    return memoizeDatelessFilters(datelessFilters);
  };
};

/**
 * Creates a selector that returns just the date filters.
 * Changes to other filters won't result in a new filters object.
 */
const createDateFiltersSelector = () => {
  const memoizeDateFilters = defaultMemoize((year, month) => ({
    years: year || [],
    months: (month == null || month.length === 0) ? range(1, 12) : month,
  }));
  return ({ year, month }) => memoizeDateFilters(year, month);
};

// eslint-disable-next-line no-undef
class CorruptionRiskDashboard extends React.Component {
  constructor(...args) {
    super(...args);
    this.state = {
      user: {
        loggedIn: false,
        isAdmin: false,
      },
      indicatorTypesMapping: {},
      filters: {},
      width: 0,
      data: Map(),
      showLandingPopup: !localStorage.alreadyVisited,
    };

    localStorage.alreadyVisited = true;

    this.selectDateFilters = createDateFiltersSelector();
    this.selectDatelessFilters = createDatelessFiltersSelector();
    this.selectDatelessFiltersForSupplier = createDatelessFiltersSelector(['supplierId']);
    this.selectDatelessFiltersForPE = createDatelessFiltersSelector(['procuringEntityId']);
  }

  componentDidMount() {
    this.fetchUserInfo();
    this.fetchIndicatorTypesMapping();

    // eslint-disable-next-line react/no-did-mount-set-state
    this.setState({
      width: document.querySelector('.content').offsetWidth - 30,
    });

    window.addEventListener('resize', debounce(() => {
      this.setState({
        width: document.querySelector('.content').offsetWidth - 30,
      });
    }));
  }

  getPage() {
    const styling = this.constructor.STYLING || this.props.styling;
    const { page, type, individualIndicator } = this.props.match.params;

    const { indicatorTypesMapping, width } = this.state;

    if (page === 'type') {
      const indicators = getIndicators(indicatorTypesMapping, type);

      return (
        <CorruptionTypePage
          {...this.wireProps(['corruptionType', type])}
          indicators={indicators}
          onGotoIndicator={(individualIndicator) => this.props.history.push(`/ui/crd/indicator/${type}/${individualIndicator}`)}
          corruptionType={type}
          width={width}
          styling={styling}
        />
      );
    } if (page === 'indicator') {
      return (
        <IndividualIndicatorPage
          {...this.wireProps(['indicator', individualIndicator])}
          indicator={individualIndicator}
          corruptionType={type}
          width={width}
          styling={styling}
        />
      );
    } if (page === 'contracts') {
      return this.renderArchive(ContractsPage, 'contracts');
    } if (page === 'contract') {
      return this.renderSingle({
        Component: ContractPage,
        sgSlug: 'contract',
        plSlug: 'contracts',
        additionalProps: {
          totalContracts: this.state.data.getIn(['sidebar', 'totalFlags', 'contractCounter'], 0),
        },
      });
    } if (page === 'suppliers') {
      return this.renderArchive(SuppliersPage, 'suppliers');
    } if (page === 'supplier') {
      return this.renderSingle({
        Component: SupplierPage,
        sgSlug: 'supplier',
        plSlug: 'suppliers',
        selectDatelessFiltersFn: this.selectDatelessFiltersForSupplier,
      });
    } if (page === 'procuring-entities') {
      return this.renderArchive(ProcuringEntitiesPage, 'procuring-entities');
    } if (page === 'buyers') {
      return this.renderArchive(BuyersPage, 'buyers');
    } if (page === 'procuring-entity') {
      return this.renderSingle({
        Component: ProcuringEntityPage,
        sgSlug: 'procuring-entity',
        plSlug: 'procuring-entities',
        selectDatelessFiltersFn: this.selectDatelessFiltersForPE,
      });
    } if (page === 'buyer') {
      return this.renderSingle({
        Component: BuyerPage,
        sgSlug: 'buyer',
        plSlug: 'buyers',
      });
    }
    return (
      <OverviewPage
        {...this.wireProps('overview')}
        indicatorTypesMapping={indicatorTypesMapping}
        styling={styling}
        width={width}
      />
    );
  }

  wireProps(_slug, selectDatelessFiltersFn) {
    const slug = Array.isArray(_slug) ? _slug : [_slug];
    const { t } = this.props;
    const { filters, width } = this.state;
    const datelessFilters = selectDatelessFiltersFn
      ? selectDatelessFiltersFn(filters)
      : this.selectDatelessFilters(filters);
    const { years, months } = this.selectDateFilters(filters);

    return {
      t,
      data: this.state.data.getIn(slug, Map()),
      requestNewData: (path, newData) => this.setState(
        (state) => ({ data: state.data.setIn(slug.concat(path), newData) }),
      ),
      filters: datelessFilters,
      years,
      monthly: years.length === 1,
      months,
      width,
    };
  }

  fetchUserInfo() {
    const noCacheUrl = new URI('/isAuthenticated').addSearch('time', Date.now());
    fetchJson(noCacheUrl)
      .then(({ authenticated, disabledApiSecurity }) => {
        this.setState({
          user: {
            loggedIn: authenticated,
          },
          showLandingPopup: !authenticated || disabledApiSecurity,
          disabledApiSecurity,
        });
      });
  }

  fetchIndicatorTypesMapping() {
    fetchJson('/api/indicatorTypesMapping')
      .then((data) => this.setState({ indicatorTypesMapping: data }));
  }

  loginBox() {
    const { t } = this.props;
    if (this.state.user.loggedIn) {
      return (
        <a href="/preLogout?referrer=/ui/crd/">
          <button className="btn btn-success">
            {t('general:logout')}
          </button>
        </a>
      );
    }
    const hash = encodeURIComponent(window.location.hash);
    return (
      <a href={`${LOGIN_URL}${hash}`}>
        <button className="btn btn-success">
          {t('general:login')}
        </button>
      </a>
    );
  }

  languageSwitcher() {
    const { i18n } = this.props;
    if (LOCALES.length <= 1) return null;
    return LOCALES.map((locale) => (
      <a
        key={locale}
        onClick={() => i18n.changeLanguage(locale)}
        className={cn({ active: locale === i18n.language })}
      >
        {locale.split('_')[0]}
      </a>
    ));
  }

  renderArchive(Component, slug) {
    const { type } = this.props.match.params;

    return (
      <Component
        {...this.wireProps(slug)}
        searchQuery={type}
        doSearch={(query) => this.props.history.push(`/ui/crd/${slug}/${query}`)}
        styling={this.props.styling}
      />
    );
  }

  renderSingle({
    Component, sgSlug, plSlug, additionalProps, selectDatelessFiltersFn,
  }) {
    const { styling } = this.props;
    const { indicatorTypesMapping } = this.state;
    const { type } = this.props.match.params;
    return (
      <Component
        {...this.wireProps(sgSlug, selectDatelessFiltersFn)}
        id={type}
        styling={styling}
        doSearch={(query) => this.props.history.push(`/ui/crd/${plSlug}/${query}/`)}
        indicatorTypesMapping={indicatorTypesMapping}
        {...additionalProps}
      />
    );
  }

  render() {
    const {
      filters,
      indicatorTypesMapping, showLandingPopup,
      disabledApiSecurity,
    } = this.state;

    const { t } = this.props;
    const { page } = this.props.match.params;

    return (
      <div className="container-fluid dashboard-corruption-risk">
        {showLandingPopup
        && (
          <LandingPopup
            redirectToLogin={!disabledApiSecurity}
            requestClosing={() => this.setState({ showLandingPopup: false })}
            t={t}
            languageSwitcher={(...args) => this.languageSwitcher(...args)}
          />
        )}
        <header className="branding row">
          <div className="col-sm-10 logo-wrapper">
            <a className="portal-logo-wrapper" href="/ui/">
              <img src={makueniLogo} alt="Makueni" />
              <span>{t('crd:title')}</span>
            </a>
          </div>

          <div className="col-sm-2 header-right">
            <span className="login-wrapper">
              {!disabledApiSecurity && this.loginBox()}
            </span>
          </div>
        </header>

        <Filters
          onChange={(filtersToApply) => {
            this.setState({
              filters: filtersToApply,
            });
          }}
          filters={filters}
        />

        <Sidebar
          {...this.wireProps('sidebar')}
          page={page}
          indicatorTypesMapping={indicatorTypesMapping}
          styling={this.props.styling}
        />
        <div className="row">
          <div className="col-sm-offset-3 col-sm-9 content">
            {this.getPage()}
          </div>
        </div>
      </div>
    );
  }
}

CorruptionRiskDashboard.propTypes = {
  styling: PropTypes.object.isRequired,
  t: PropTypes.func.isRequired,
  i18n: PropTypes.object.isRequired,
};

export default withRouter(CorruptionRiskDashboard);
