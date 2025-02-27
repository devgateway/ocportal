import 'bootstrap/dist/css/bootstrap.min.css';
import 'leaflet/dist/leaflet.css';
import './i18n';

document.title = `${process.env.REACT_APP_TITLE}` || 'Makueni Open Contracting Portal';
require('./oce-standalone/index');
