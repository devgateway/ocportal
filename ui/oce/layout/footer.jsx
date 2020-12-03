import React from 'react';
import translatable from '../translatable';

import 'intro.js/introjs.css';
import './header.less';

export default class Footer extends translatable(React.Component) {
  render() {
    return (
    <footer className="col-sm-12 main-footer text-center text-muted">
      {this.t("footer:productName")}
      &nbsp;-&nbsp;
      <a href="https://github.com/devgateway/forms-makueni/blob/master/LICENSE" target="_blank">{this.t("footer:license")}</a>
      &nbsp;-&nbsp;
      <a href="#!/publication-policy">{this.t("footer:publicationPolicy")}</a>
    </footer>);
  }
}
