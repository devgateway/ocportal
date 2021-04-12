import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { useTranslation } from 'react-i18next';
import { enableMapSet } from 'immer';
import {
  BrowserRouter as Router,
  Switch,
  Route, Redirect,
} from 'react-router-dom';
import ViewSwitcher from '../oce/switcher';
import './style.scss';
import OCEMakueni from './oceMakueni';
import MakueniTenders from '../oce/makueni/tenders/makueniTenders';
import MakueniProcurementPlans from '../oce/makueni/procurementPlan/makueniProcurementPlans';
import CorruptionRickDashboard from '../oce/corruption-risk';

import 'react-bootstrap-typeahead/css/Typeahead.css';
import Alerts from '../oce/alerts/Alerts';
import Docs from '../oce/makueni/Docs';
import PublicationPolicy from '../oce/makueni/PublicationPolicy';
import ContractsList from '../oce/makueni/ContractsList';
import SMSHelp from '../oce/makueni/SMSHelp';
import PortalVideos from '../oce/makueni/PortalVideos';

import store from '../oce/app/store';

enableMapSet();

/**
 * Can be used to debug routes more easily
 */
// class DebugRouter extends Router {
//   constructor(props) {
//     super(props);
//     console.log('initial history is: ', JSON.stringify(this.history, null, 2));
//     this.history.listen((location, action) => {
//       console.log(`The current URL is ${location.pathname}${location.search}${location.hash}`);
//       console.log(`The last navigation action was ${action}`, JSON.stringify(this.history, null, 2));
//     });
//   }
// }

const BILLION = 1000000000;
const MILLION = 1000000;
const THOUSAND = 1000;
const formatNumber = (number) => (number === undefined ? number : number.toLocaleString(undefined, { maximumFractionDigits: 3 }));

const formatDate = (stringDate) => {
  if (stringDate === undefined) {
    return undefined;
  }

  const date = new Date(stringDate);
  let dd = date.getDate();
  let mm = date.getMonth() + 1;
  const yyyy = date.getFullYear().toString().substr(-2);

  if (dd < 10) {
    dd = `0${dd}`;
  }

  if (mm < 10) {
    mm = `0${mm}`;
  }

  return `${dd}/${mm}/${yyyy}`;
};

const formatBoolean = (bool) => {
  if (bool === undefined) {
    return '';
  }
  if (bool) {
    return 'YES';
  }
  return 'NO';
};

const styling = {
  charts: {
    axisLabelColor: '#000000',
    traceColors: ['#324d6e', '#ecac00', '#4b6f33'],
    hoverFormat: ',.2f',
    hoverFormatter: (number) => {
      if (typeof number === 'undefined') return number;
      const abs = Math.abs(number);
      if (abs >= BILLION) return `${formatNumber(number / BILLION)}B`;
      if (abs >= MILLION) return `${formatNumber(number / MILLION)}M`;
      if (abs >= THOUSAND) return `${formatNumber(number / THOUSAND)}K`;
      return formatNumber(number);
    },
  },
  tables: {
    currencyFormatter: formatNumber,
    formatDate,
    formatBoolean,
  },
};

CorruptionRickDashboard.STYLING = JSON.parse(JSON.stringify(styling));
CorruptionRickDashboard.STYLING.charts.traceColors = ['#3371b1', '#2b9ff6', '#5db7fb', '#86cafd', '#bbe2ff'];

class OceSwitcher extends ViewSwitcher {
}

OceSwitcher.views.tender = MakueniTenders;
OceSwitcher.views.docs = Docs;
OceSwitcher.views.smshelp = SMSHelp;
OceSwitcher.views['publication-policy'] = PublicationPolicy;
OceSwitcher.views['portal-videos'] = PortalVideos;
OceSwitcher.views['contracts-list'] = ContractsList;
OceSwitcher.views['procurement-plan'] = MakueniProcurementPlans;
OceSwitcher.views.alerts = Alerts;
OceSwitcher.views['m-and-e'] = OCEMakueni;
OceSwitcher.views.crd = CorruptionRickDashboard;

// this could be replaced with Suspense
const OceSwitcherLoader = () => {
  const { t, i18n, ready } = useTranslation();
  return ready
    ? (
      <Router>
        <Switch>
          <Route exact path="/ui">
            <Redirect to="/ui/tender" />
          </Route>
          <Route path="/ui/alerts/:preqId?">
            <Alerts
              styling={styling}
              t={t}
              i18n={i18n}
            />
          </Route>
          <Route path="/ui/publication-policy">
            <PublicationPolicy
              styling={styling}
              t={t}
              i18n={i18n}
            />
          </Route>
          <Route path="/ui/tender">
            <MakueniTenders
              styling={styling}
              t={t}
              i18n={i18n}
            />
          </Route>
          <Route path="/ui/procurement-plan">
            <MakueniProcurementPlans
              styling={styling}
              t={t}
              i18n={i18n}
            />
          </Route>
          <Route path="/ui/m-and-e/:selected?">
            <OCEMakueni
              styling={styling}
              t={t}
              i18n={i18n}
            />
          </Route>
          <Route path="/ui/docs">
            <Docs
              styling={styling}
              t={t}
              i18n={i18n}
            />
          </Route>
          <Route path="/ui/crd/:page?/:type?/:individualIndicator?">
            <CorruptionRickDashboard
              styling={styling}
              t={t}
              i18n={i18n}
            />
          </Route>
          <Route path="/ui/procurement-plan">
            <MakueniProcurementPlans
              styling={styling}
              t={t}
              i18n={i18n}
            />
          </Route>
        </Switch>
      </Router>
    )
    : null;
};

ReactDOM.render(
  <Provider store={store}>
    <OceSwitcherLoader />
  </Provider>,
  document.getElementById('dg-container'),
);
