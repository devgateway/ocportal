import { Map } from 'immutable';
import cn from 'classnames';
import OCApp from '../oce';
import OverviewTab from '../oce/tabs/overview';
import LocationTab from '../oce/tabs/location';
import CompetitivenessTab from '../oce/tabs/competitiveness';
import EfficiencyTab from '../oce/tabs/efficiency';
import EProcurementTab from '../oce/tabs/e-procurement';
import { fetchJson } from '../oce/tools';
import Header from '../oce/layout/header';

class OCEMakueni extends OCApp {
  constructor(props) {
    super(props);
    this.registerTab(OverviewTab);
    // this.registerTab(OCEDemoLocation);
    this.registerTab(CompetitivenessTab);
    this.registerTab(EfficiencyTab);
    this.registerTab(EProcurementTab);
  }

  fetchBidTypes() {
    fetchJson('/api/ocds/bidType/all')
    .then(data =>
      this.setState({
        bidTypes: data.reduce((map, datum) =>
          map.set(datum.id, datum.description), Map())
      })
    );
  }

  loginBox() {
    let linkUrl;
    let text;
    if (this.state.user.loggedIn) {
      linkUrl = '/preLogout?referrer=/ui/index.html';
      text = this.t('general:logout');
    } else {
      linkUrl = '/login?referrer=/ui/index.html';
      text = this.t('general:login');
    }
    return (
      <a href={linkUrl} className="login-logout">
        <button className="btn btn-default">
          {text}
        </button>
      </a>
    );
  }

  dashboardSwitcher() {
    const { dashboardSwitcherOpen } = this.state;
    const { onSwitch } = this.props;
    return (
      <div className={cn('dash-switcher-wrapper', { open: dashboardSwitcherOpen })}>
        <h1 onClick={this.toggleDashboardSwitcher.bind(this)}>
          <strong>Monitoring & Evaluation</strong> Toolkit
          <i className="glyphicon glyphicon-menu-down"/>
        </h1>
        {dashboardSwitcherOpen &&
        <div className="dashboard-switcher">
          <a href="javascript:void(0);" onClick={() => onSwitch('crd')}>
            Corruption Risk Dashboard
          </a>
        </div>
        }
      </div>
    );
  }

  exportBtn() {
    if (this.state.exporting) {
      return (
        <div className="export-progress">
          <div className="progress">
            <div className="progress-bar progress-bar-danger" role="progressbar"
                 style={{ width: '100%' }}>
              {this.t('export:exporting')}
            </div>
          </div>
        </div>
      );
    }
    return (<div>
        <span className="export-title">
          Download the Data
        </span>
        <div className="export-btn">
          <button className="btn btn-default" disabled>
          </button>
        </div>
      </div>
    );
  }

  languageSwitcher() {
    const { TRANSLATIONS } = this.constructor;
    const { locale: selectedLocale } = this.state;
    if (Object.keys(TRANSLATIONS).length <= 1) return null;
    return Object.keys(TRANSLATIONS)
    .map(locale => (
      <a key={locale}
         href="javascript:void(0);"
         onClick={() => this.setLocale(locale)}
         className={cn({ active: locale === selectedLocale })}
      >
        {locale.split('_')[0]}
      </a>
    ));
  }

  render() {
    return (
      <div className="container-fluid dashboard-default">
        <Header translations={this.props.translations} onSwitch={this.props.onSwitch}
                selected="m-and-e"/>

        <div className="row content">
          <div className="col-xs-4 col-md-3 menu">
            <div className="row">
              <div className="filters-hint col-md-12">
                {this.t('filters:hint')}
              </div>
              {this.filters()}
              {this.comparison()}
            </div>
          </div>

          <div className="col-xs-8 col-md-9">
            <div className="row">
              <div className="navigation">
                {this.navigation()}
              </div>
            </div>

            <div className="row">
              {this.content()}
            </div>
          </div>
        </div>

        {this.showMonths() && <div
          className="col-xs-offset-4 col-md-offset-3 col-xs-8 col-md-9 months-bar"
          role="navigation"
        >
          {this.monthsBar()}
        </div>}
        <div
          className="col-xs-offset-4 col-md-offset-3 col-xs-8 col-md-9 years-bar"
          role="navigation"
        >
          {this.yearsBar()}
        </div>
        <footer className="col-sm-12 main-footer">&nbsp;</footer>
      </div>
    );
  }
}


class OCEDemoLocation extends LocationTab {
  getHeight() {
    const TOP_OFFSET = 128;
    const BOTTOM_OFFSET = 66;
    return window.innerHeight - TOP_OFFSET - BOTTOM_OFFSET;
  }
}

OCEDemoLocation.CENTER = [37, -100];

export default OCEMakueni;
