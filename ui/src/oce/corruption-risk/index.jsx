import React from 'react';
import cn from 'classnames';
import URI from 'urijs';
import { Map } from 'immutable';
import PropTypes from 'prop-types';
import { defaultMemoize } from 'reselect';
import { isEqualWith } from 'lodash';
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
import enTranslations from '../../languages/en_US.json';
import esTranslations from '../../languages/es_ES.json';
import frTranslations from '../../languages/fr_FR.json';

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
const createDatelessFiltersSelector = () => {
  const memoizeDatelessFilters = defaultMemoize((filters) => (filters), isShallowEqual);
  return ({ year, month, ...datelessFilters }) => memoizeDatelessFilters(datelessFilters);
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
    const { oceLocale } = localStorage;
    if (oceLocale && this.constructor.TRANSLATIONS[oceLocale]) {
      this.state.locale = oceLocale;
    } else {
      this.state.locale = 'en_US';
    }

    localStorage.alreadyVisited = true;

    this.selectDateFilters = createDateFiltersSelector();
    this.selectDatelessFilters = createDatelessFiltersSelector();
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

  setLocale(locale) {
    this.setState({ locale });
    localStorage.oceLocale = locale;
  }

  getPage() {
    const { route, navigate } = this.props;
    const styling = this.constructor.STYLING || this.props.styling;
    const [page] = route;

    const { indicatorTypesMapping, width } = this.state;

    if (page === 'type') {
      const [, corruptionType] = route;

      const indicators = getIndicators(indicatorTypesMapping, corruptionType);

      return (
        <CorruptionTypePage
          {...this.wireProps(['corruptionType', corruptionType])}
          indicators={indicators}
          onGotoIndicator={(individualIndicator) => navigate('indicator', corruptionType, individualIndicator)}
          corruptionType={corruptionType}
          width={width}
          styling={styling}
        />
      );
    } if (page === 'indicator') {
      const [, corruptionType, individualIndicator] = route;
      return (
        <IndividualIndicatorPage
          {...this.wireProps(['indicator', individualIndicator])}
          indicator={individualIndicator}
          corruptionType={corruptionType}
          width={width}
          styling={styling}
          navigate={navigate}
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
        navigate={navigate}
      />
    );
  }

  getTranslations() {
    const { TRANSLATIONS } = this.constructor;
    const { locale } = this.state;
    return TRANSLATIONS[locale];
  }

  wireProps(_slug) {
    const slug = Array.isArray(_slug) ? _slug : [_slug];
    const translations = this.getTranslations();
    const { filters, width } = this.state;
    const datelessFilters = this.selectDatelessFilters(filters);
    const { years, months } = this.selectDateFilters(filters);

    return {
      translations,
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

  t(str) {
    const { locale } = this.state;
    const { TRANSLATIONS } = this.constructor;
    return TRANSLATIONS[locale][str] || TRANSLATIONS.en_US[str] || str;
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
    if (this.state.user.loggedIn) {
      return (
        <a href="/preLogout?referrer=/ui/index.html?corruption-risk-dashboard">
          <button className="btn btn-success">
            {this.t('general:logout')}
          </button>
        </a>
      );
    }
    const hash = encodeURIComponent(window.location.hash);
    return (
      <a href={`${LOGIN_URL}${hash}`}>
        <button className="btn btn-success">
          {this.t('general:login')}
        </button>
      </a>
    );
  }

  languageSwitcher() {
    const { TRANSLATIONS } = this.constructor;
    const { locale: selectedLocale } = this.state;
    if (Object.keys(TRANSLATIONS).length <= 1) return null;
    return Object.keys(TRANSLATIONS)
      .map((locale) => (
        <a
          onClick={() => this.setLocale(locale)}
          className={cn({ active: locale === selectedLocale })}
        >
          {locale.split('_')[0]}
        </a>
      ));
  }

  renderArchive(Component, slug) {
    const { navigate, route } = this.props;
    const [, searchQuery] = route;
    return (
      <Component
        {...this.wireProps(slug)}
        searchQuery={searchQuery}
        doSearch={(query) => navigate(slug, query)}
        navigate={navigate}
        styling={this.props.styling}
      />
    );
  }

  renderSingle({
    Component, sgSlug, plSlug, additionalProps,
  }) {
    const { route, navigate, styling } = this.props;
    const { indicatorTypesMapping } = this.state;
    const [, id] = route;
    return (
      <Component
        {...this.wireProps(sgSlug)}
        id={id}
        styling={styling}
        doSearch={(query) => navigate(plSlug, query)}
        navigate={navigate}
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

    const { route, navigate } = this.props;
    const translations = this.getTranslations();
    const [page] = route;

    return (
      <div className="container-fluid dashboard-corruption-risk">
        {showLandingPopup
        && (
        <LandingPopup
          redirectToLogin={!disabledApiSecurity}
          requestClosing={() => this.setState({ showLandingPopup: false })}
          translations={translations}
          languageSwitcher={(...args) => this.languageSwitcher(...args)}
        />
        )}
        <header className="branding row">
          <div className="col-sm-10 logo-wrapper">
            <a className="portal-logo-wrapper" href="#!/crd/">
              <img src={makueniLogo} alt="Makueni" />
              <span>{this.t('crd:title')}</span>
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
          translations={translations}
          filters={filters}
        />

        <Sidebar
          {...this.wireProps('sidebar')}
          page={page}
          indicatorTypesMapping={indicatorTypesMapping}
          route={route}
          navigate={navigate}
          styling={this.props.styling}
        />
        <div className="col-sm-offset-3 col-sm-9 content">
          {this.getPage()}
        </div>
      </div>
    );
  }
}

CorruptionRiskDashboard.propTypes = {
  translations: PropTypes.object.isRequired,
  styling: PropTypes.object.isRequired,
  onSwitch: PropTypes.func.isRequired,
  route: PropTypes.array.isRequired,
  navigate: PropTypes.func.isRequired,
};

CorruptionRiskDashboard.TRANSLATIONS = {
  en_US: enTranslations,
  es_ES: esTranslations,
  fr_FR: frTranslations,
};

export default CorruptionRiskDashboard;
