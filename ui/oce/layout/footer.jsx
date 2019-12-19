import React from 'react';
import translatable from '../translatable';
import cn from 'classnames';
import URI from 'urijs';
import Cookies from 'universal-cookie';

import * as introJs from 'intro.js/intro.js';
import 'intro.js/introjs.css';
import './header.less';
import { statsInfo } from './state';

export default class Footer extends translatable(React.Component) {
  render() {
    return (
    <footer className="col-sm-12 main-footer text-center text-muted">
      Government of Makueni County Open Contracting Portal&nbsp;-&nbsp;
      <a href="https://github.com/devgateway/forms-makueni/blob/master/LICENSE" target="_blank">License</a>
      &nbsp;-&nbsp;
      <a href="#!/publication-policy">Publication Policy</a>
    </footer>);
  }
}
