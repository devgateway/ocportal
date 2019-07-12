import ReactDOM from 'react-dom';
import ViewSwitcher from '../oce/switcher.jsx';
import './style.less';
import OCEMakueni from './oceMakueni';
import MakueniTenders from '../oce/makueni/tenders/makueniTenders';
import CorruptionRickDashboard from '../oce/corruption-risk';

const translations = {
  en_US: require('../../web/public/languages/en_US.json'),
  es_ES: require('../../web/public/languages/es_ES.json'),
  fr_FR: require('../../web/public/languages/fr_FR.json'),
};

const BILLION = 1000000000;
const MILLION = 1000000;
const THOUSAND = 1000;
const formatNumber = number => number.toLocaleString(undefined, { maximumFractionDigits: 2 });

const styling = {
  charts: {
    axisLabelColor: '#cc3c3b',
    traceColors: ['#324d6e', '#ecac00', '#4b6f33'],
    hoverFormat: ',.2f',
    hoverFormatter: (number) => {
      if (typeof number === 'undefined') return number;
      let abs = Math.abs(number);
      if (abs >= BILLION) return formatNumber(number / BILLION) + 'B';
      if (abs >= MILLION) return formatNumber(number / MILLION) + 'M';
      if (abs >= THOUSAND) return formatNumber(number / THOUSAND) + 'K';
      return formatNumber(number);
    },
  },
  tables: {
    currencyFormatter: formatNumber,
  },
};

OCEMakueni.STYLING = styling;
OCEMakueni.TRANSLATIONS = translations;

CorruptionRickDashboard.STYLING = JSON.parse(JSON.stringify(styling));
CorruptionRickDashboard.STYLING.charts.traceColors = ['#234e6d', '#3f7499', '#80b1d3', '#afd5ee', '#d9effd'];

class OceSwitcher extends ViewSwitcher {
}

OceSwitcher.views['makueni'] = MakueniTenders;
OceSwitcher.views['m-and-e'] = OCEMakueni;
OceSwitcher.views.crd = CorruptionRickDashboard;

ReactDOM.render(<OceSwitcher
  translations={translations.en_US}
  styling={styling}
/>, document.getElementById('dg-container'));
