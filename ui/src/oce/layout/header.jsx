import React from 'react';
import translatable from '../translatable';
import cn from 'classnames';
import URI from 'urijs';
import Cookies from 'universal-cookie';

import * as introJs from 'intro.js/intro.js';
import 'intro.js/introjs.css';
import './header.scss';
import {statsInfo} from './state';
import ReactGA from 'react-ga';

export default class Header extends translatable(React.Component) {
  constructor(props) {
    super(props);

    this.state = {
      selected: props.selected || '',
    };

    this.tabs = [
      {
        name: 'tender',
        title: this.t("header:tender"),
        step:2,
        intro: this.t("header:tender:intro"),
      },
      {
        name: 'procurement-plan',
        title: this.t("header:procurementPlan"),
        step:3,
        intro: this.t("header:procurementPlan:intro"),
      },
      {
        name: 'm-and-e',
        title: this.t("header:me"),
        step:4,
        intro: this.t("header:me:intro"),
      },
      {
        name: 'docs',
        title: this.t("header:docs"),
        step:5,
        intro: this.t("header:docs:intro")
      }
    ];

    this.changeOption = this.changeOption.bind(this);
    this.isActive = this.isActive.bind(this);
  }

  initializeGoogleAnalytics() {
    ReactGA.initialize('UA-154640611-1');
    let prefix = window.location.protocol + '//' + window.location.host;
    let href = window.location.href;
    ReactGA.pageview(href.substring(prefix.length, href.length));
  }

  componentDidMount() {
    this.initializeGoogleAnalytics();
    statsInfo.addListener('Header', () => {
      statsInfo.getState()
      .then(data => {
        this.setState({ data: data });
        const cookies = new Cookies();
        if (cookies.get("introjs") === undefined ) {
          const current = new Date();
          const nextYear = new Date();
          nextYear.setFullYear(current.getFullYear() + 1);
          cookies.set("introjs","introjs", {path:'/', expires: nextYear});
          this.showIntroJs();
        }
      });
    });
  }

  componentWillUnmount() {
    statsInfo.removeListener('Header');
  }

  changeOption(option) {
    this.setState({ selected: option });
    this.props.onSwitch(option);
  }

  isActive(option) {
    const { selected } = this.state;
    if (selected === '') {
      return false;
    }
    return selected === option;
  }

  showIntroJs() {
    window.scrollTo(0, 0);
    introJs.introJs()
      .onbeforeexit(() => {
        window.removeEventListener('scroll', this.noScroll);
        return true;
      })
      .setOption('overlayOpacity', 0.7)
      .setOption('showProgress', true)
      .setOption('scrollToElement', true)
      .start();
  }

  noScroll() {
    window.scrollTo(0, 0);
  }

  exportBtn() {
    const excelURL = new URI('/api/makueni/excelExport');
    const jsonURL = new URI('/api/ocds/package/all');

    return (<div>
        <span className="download-title" data-step="7" data-intro={this.t("header:export:intro")}>
          {this.t("header:export")}
        </span>
        <div className="export-btn">
          <a href={excelURL} download="export.zip">
            <button className="xls"></button>
          </a>
          <a href={jsonURL} target="_blank">
            <button className="json"></button>
          </a>
        </div>
      </div>
    );
  }

  render() {
    const { data } = this.state;
    const currencyFormatter = this.props.styling.tables.currencyFormatter;

    return (<div>
      <header className="branding row">
        <div className="col-md-6 col-sm-6 col-xs-12">
          <a className="portal-logo-wrapper" href="#!/">
              <img src={process.env.PUBLIC_URL + "/makueni-logo.png"} alt="Makueni"/>
              <span data-step="1" data-intro={this.t("header:title:intro")}>
                {this.t("header:title")}</span>
          </a>
        </div>

        <div className="col-md-6 col-sm-6 col-xs-12">
          <div className="row">
            <div className="navigation">
              {
                this.tabs.map(tab => {
                  return (<a
                      key={tab.name}
                      className={cn('', { active: this.isActive(tab.name) })}
                      onClick={() => this.changeOption(tab.name)}
                    ><span data-intro={tab.intro} data-step={tab.step}>
                      {tab.title}
                    </span>
                    </a>
                  );
                })
              }
              <a key="HELP" onClick={() => {
                window.addEventListener('scroll', this.noScroll);
                this.showIntroJs();
              }
              }>
                <span data-intro={this.t("header:help:intro")} data-step="6">{this.t("header:help")}</span>
              </a>
            </div>
          </div>
        </div>
      </header>

      <div className="header-tools row">
        {
          data !== undefined
            ? <div>
              <div className="col-lg-3 col-md-3 col-sm-6 total-item" data-step="5" data-intro={this.t("header:totalContracts:intro")} data-position="right">
                <span className="total-label">{this.t("header:totalContracts")}</span>
                <span className="total-number">{data.count}</span>
              </div>
              <div className="col-lg-5 col-md-5 col-sm-6 total-item" data-step="6"
                   data-intro={this.t("header:totalContractsAmount:intro")}
                   data-position="right">
                <span className="total-label">{this.t("header:totalContractsAmount")}</span>
                <span className="total-number">{currencyFormatter(data.value)}</span>
              </div>
            </div>
            : null
        }

        <div className="col-md-4 col-sm-12 col-xs-12 export">
          {this.exportBtn()}
        </div>
      </div>
    </div>);
  }
}
