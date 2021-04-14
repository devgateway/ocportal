import React from 'react';
import { Trans } from 'react-i18next';

import 'intro.js/introjs.css';
import './header.scss';
import { Link } from 'react-router-dom';

export default class Footer extends React.Component {
  render() {
    return (
      <footer className="col-sm-12 main-footer text-center text-muted">
        <Trans i18nKey="footer:productName" />
      &nbsp;-&nbsp;
        <a href="https://github.com/devgateway/forms-makueni/blob/master/LICENSE" target="_blank">
          <Trans i18nKey="footer:license" />
        </a>
      &nbsp;-&nbsp;
        <Link to="/portal/publication-policy">
          <Trans i18nKey="footer:publicationPolicy" />
        </Link>
      </footer>
    );
  }
}
