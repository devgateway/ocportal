import 'bootstrap/dist/css/bootstrap.min.css';
import 'leaflet/dist/leaflet.css';
import './i18n';

document.title = `${process.env.ORG_NAME} Open Contracting Portal` || 'Makueni Open Contracting Portal';
require('./oce-standalone/index');
