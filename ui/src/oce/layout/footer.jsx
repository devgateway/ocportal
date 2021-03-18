import React from 'react';
import { Trans } from 'react-i18next';

import 'intro.js/introjs.css';
import './header.scss';

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
        <a href="#!/publication-policy">
          <Trans i18nKey="footer:publicationPolicy" />
        </a>
      </footer>
    );
  }
}
