import React from 'react';
import { Map } from 'immutable';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router-dom';
import OCApp from '../oce';
import OverviewTab from '../oce/tabs/overview';
import LocationTab from '../oce/tabs/location';
import CompetitivenessTab from '../oce/tabs/competitiveness';
import EfficiencyTab from '../oce/tabs/efficiency';
import { fetchJson } from '../oce/tools';
import Header from '../oce/layout/header';
import Footer from '../oce/layout/footer';
import Implementation from '../oce/tabs/implementation';
import fmConnect from '../oce/fm/fm';

class OCEDemoLocation extends LocationTab {
  getHeight() {
    const TOP_OFFSET = 128;
    const BOTTOM_OFFSET = 66;
    return window.innerHeight - TOP_OFFSET - BOTTOM_OFFSET;
  }
}

OCEDemoLocation.CENTER = [37, -100];

class OCEMakueni extends OCApp {
  constructor(props) {
    super(props);
    this.registerTab(OverviewTab);
    this.registerTab(OCEDemoLocation);
    this.registerTab(CompetitivenessTab);
    this.registerTab(EfficiencyTab);
    this.registerTab(Implementation);
    // this.registerTab(EProcurementTab);
  }

  fetchBidTypes() {
    fetchJson('/api/ocds/bidType/all')
      .then((data) => this.setState({
        bidTypes: data.reduce((map, datum) => map.set(datum.id, datum.description), Map()),
      }));
  }

  loginBox() {
    const { t } = this.props;
    let linkUrl;
    let text;
    if (this.state.user.loggedIn) {
      linkUrl = '/preLogout?referrer=/portal/';
      text = t('general:logout');
    } else {
      linkUrl = '/login?referrer=/portal/';
      text = t('general:login');
    }
    return (
      <a href={linkUrl} className="login-logout">
        <button className="btn btn-default">
          {text}
        </button>
      </a>
    );
  }

  exportBtn() {
    const { t } = this.props;
    if (this.state.exporting) {
      return (
        <div className="export-progress">
          <div className="progress">
            <div
              className="progress-bar progress-bar-danger"
              role="progressbar"
              style={{ width: '100%' }}
            >
              {t('export:exporting')}
            </div>
          </div>
        </div>
      );
    }
    return (
      <div>
        <span className="export-title">
          Download the Data
        </span>
        <div className="export-btn">
          <button className="btn btn-default" disabled />
        </div>
      </div>
    );
  }

  render() {
    const { t } = this.props;
    return (
      <div className="container-fluid dashboard-default">
        <Header
          styling={this.props.styling}
          selected="m-and-e"
        />
        <div className="row content charts-content">
          <div className="col-md-3 col-sm-12 menu">
            <div
              className="row"
              data-intro="On each page there is a set of filters that allows you
            to limit what information is shown on the page by selected metrics, such as a specific
            location or department."
              data-step="8"
            >
              <div className="filters-hint col-md-12">
                {t('filters:hint')}
              </div>
              {this.filters()}
              {/* {this.comparison()} */}
            </div>
          </div>
          <div className="col-md-9 col-sm-12 col-main-content">
            <div className="row">
              <div
                className="navigation subnav"
                data-intro="Navigate between the
              different types of charts, including an overview, competitiveness, and efficiency."
              >
                {this.navigation()}
              </div>
            </div>

            <div className="row">
              {this.content()}
            </div>
          </div>
        </div>
        <Footer />
      </div>
    );
  }
}

OCEMakueni.propTypes = {
  t: PropTypes.func.isRequired,
};

export default withRouter(fmConnect(OCEMakueni));
