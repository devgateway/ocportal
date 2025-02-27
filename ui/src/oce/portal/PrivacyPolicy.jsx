import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import './portal.scss';
import React from 'react';
import { Trans } from 'react-i18next';

class Docs extends CRDPage {
  render() {
    const { t } = this.props;
    return (
      <div className="container-fluid dashboard-default">
        <Header
          styling={this.props.styling}
          selected="docs"
        />
        <div className="client-procurement-plan content row">

          <div className="col-md-6 col-md-offset-3 text-justify">
            <h1 className="text-center">{t('privacyPolicy:title')}</h1>

            <h3>{t('privacyPolicy:section1:title')}</h3>
            <p>{t('privacyPolicy:section1:paragraph1')}</p>
            <p>{t('privacyPolicy:section1:paragraph2')}</p>
            <p>{t('privacyPolicy:section1:paragraph3')}</p>

            <h3>{t('privacyPolicy:section2:title')}</h3>
            <ol type="a">
              <li>
                {t('privacyPolicy:section2:item1')}
                <ol type="i">
                  <li>{t('privacyPolicy:section2:item1:sub1')}</li>
                  <li>{t('privacyPolicy:section2:item1:sub2')}</li>
                  <li>{t('privacyPolicy:section2:item1:sub3')}</li>
                  <li>{t('privacyPolicy:section2:item1:sub4')}</li>
                  <li>{t('privacyPolicy:section2:item1:sub5')}</li>
                </ol>
              </li>
              <li>
                <Trans>
                  privacyPolicy:section2:item2
                  <a href="https://policies.google.com/technologies/partner-sites" className="download-file">link</a>
                </Trans>
              </li>
              <li>{t('privacyPolicy:section2:item3')}</li>
              <li>{t('privacyPolicy:section2:item4')}</li>
            </ol>

            <h3>{t('privacyPolicy:section3:title')}</h3>
            <ol type="a">
              <li>{t('privacyPolicy:section3:item1')}</li>
              <li>{t('privacyPolicy:section3:item2')}</li>
            </ol>

            <h3>{t('privacyPolicy:section4:title')}</h3>
            <p>{t('privacyPolicy:section4:content')}</p>

            <h3>{t('privacyPolicy:section5:title')}</h3>
            <p>{t('privacyPolicy:section5:content')}</p>

            <h3>{t('privacyPolicy:section6:title')}</h3>
            <ol type="a">
              <li>
                <Trans>
                  privacyPolicy:section6:item1
                  <a href="mailto:info@developmentgateway.org" className="download-file">email</a>
                </Trans>
              </li>
              <li>{t('privacyPolicy:section6:item2')}</li>
            </ol>
          </div>

        </div>
      </div>
    );
  }
}

export default Docs;
