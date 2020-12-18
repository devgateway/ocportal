import React, {useEffect} from 'react';
import {tCreator} from '../translatable';
import cn from 'classnames';
import URI from 'urijs';
import Cookies from 'universal-cookie';
import * as introJs from 'intro.js/intro.js';
import 'intro.js/introjs.css';
import './header.scss';
import ReactGA from 'react-ga';
import {useDispatch, useSelector} from "react-redux";
import {LOADED, loadStats, selectStats} from "./statsSlice";
import PropTypes from 'prop-types';

const initGA = () => {
  ReactGA.initialize('UA-154640611-1');
  let prefix = window.location.protocol + '//' + window.location.host;
  let href = window.location.href;
  ReactGA.pageview(href.substring(prefix.length, href.length));
};

const handleIntroJS = () => {
  const cookies = new Cookies();
  if (cookies.get("introjs") === undefined ) {
    const current = new Date();
    const nextYear = new Date();
    nextYear.setFullYear(current.getFullYear() + 1);
    cookies.set("introjs","introjs", {path:'/', expires: nextYear});

    showIntroJs();
  }
};

const showIntroJs = () => {
  window.scrollTo(0, 0);
  introJs.introJs()
    .onbeforeexit(() => {
      window.removeEventListener('scroll', noScroll);
      return true;
    })
    .setOption('overlayOpacity', 0.7)
    .setOption('showProgress', true)
    .setOption('scrollToElement', true)
    .start();
};

const noScroll = () => window.scrollTo(0, 0);

const Header = props => {

  const t = tCreator(props.translations);

  const tabs = [
    {
      name: 'tender',
      title: t("header:tender"),
      step:2,
      intro: t("header:tender:intro"),
    },
    {
      name: 'procurement-plan',
      title: t("header:procurementPlan"),
      step:3,
      intro: t("header:procurementPlan:intro"),
    },
    {
      name: 'm-and-e',
      title: t("header:me"),
      step:4,
      intro: t("header:me:intro"),
    },
    {
      name: 'docs',
      title: t("header:docs"),
      step:5,
      intro: t("header:docs:intro")
    }
  ];

  const dispatch = useDispatch();

  useEffect(() => {
    initGA();
    handleIntroJS();
    dispatch(loadStats());
  }, [dispatch]);

  const changeOption = option => props.onSwitch(option);

  const isActive = option => props.selected === option;

  const exportBtn = () => {
    const excelURL = new URI('/api/makueni/excelExport');
    const jsonURL = new URI('/api/ocds/package/all');

    return (
      <div>
        <span className="download-title" data-step="7" data-intro={t("header:export:intro")}>
          {t("header:export")}
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
  };

  const currencyFormatter = props.styling.tables.currencyFormatter;

  const stats = useSelector(selectStats);

  return (
    <div>
      <header className="branding row">
        <div className="col-md-6 col-sm-6 col-xs-12">
          <a className="portal-logo-wrapper" href="#!/">
            <img src={process.env.PUBLIC_URL + "/makueni-logo.png"} alt="Makueni"/>
            <span data-step="1" data-intro={t("header:title:intro")}>
                {t("header:title")}</span>
          </a>
        </div>

        <div className="col-md-6 col-sm-6 col-xs-12">
          <div className="row">
            <div className="navigation">
              {
                tabs.map(tab => {
                  return (<a
                      key={tab.name}
                      className={cn('', { active: isActive(tab.name) })}
                      onClick={() => changeOption(tab.name)}
                    ><span data-intro={tab.intro} data-step={tab.step}>
                      {tab.title}
                    </span>
                    </a>
                  );
                })
              }
              <a key="HELP" onClick={() => {
                window.addEventListener('scroll', noScroll);
                showIntroJs();
              }
              }>
                <span data-intro={t("header:help:intro")} data-step="6">{t("header:help")}</span>
              </a>
            </div>
          </div>
        </div>
      </header>

      <div className="header-tools row">
        {
          stats.status === LOADED
            ? <div>
              <div className="col-lg-3 col-md-3 col-sm-6 total-item" data-step="5" data-intro={t("header:totalContracts:intro")} data-position="right">
                <span className="total-label">{t("header:totalContracts")}</span>
                <span className="total-number">{stats.totalContracts}</span>
              </div>
              <div className="col-lg-5 col-md-5 col-sm-6 total-item" data-step="6"
                   data-intro={t("header:totalContractsAmount:intro")}
                   data-position="right">
                <span className="total-label">{t("header:totalContractsAmount")}</span>
                <span className="total-number">{currencyFormatter(stats.totalContractsAmount)}</span>
              </div>
            </div>
            : null
        }

        <div className="col-md-4 col-sm-12 col-xs-12 export">
          {exportBtn()}
        </div>
      </div>
    </div>
  );
};

Header.propTypes = {
  translations: PropTypes.object.isRequired,
  selected: PropTypes.string,
  onSwitch: PropTypes.func.isRequired,
  styling: PropTypes.object.isRequired
};

export default Header;