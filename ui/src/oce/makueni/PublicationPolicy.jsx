import CRDPage from '../corruption-risk/page';
import Header from '../layout/header';
import './makueni.scss';
import React from 'react';
import reactStringReplace from 'react-string-replace';

class Docs extends CRDPage {
  render() {
    const { t } = this.props;
    return (
      <div className="container-fluid dashboard-default">
        <Header
          styling={this.props.styling}
          selected="docs"
        />
        <div className="makueni-procurement-plan content row">

          <div className="col-md-2 col-md-offset-5">
            <h1>{t('publicationPolicy:title')}</h1>
          </div>

          <div className="col-md-6 col-md-offset-3 text-justify">
            <h3>{t('publicationPolicy:pp:title')}</h3>
            {t('publicationPolicy:pp:text')}
            <h3>{t('publicationPolicy:pd:title')}</h3>
            {t('publicationPolicy:pd:text')}
            <div>
              <img src={`${process.env.PUBLIC_URL}/ocportal-architecture.svg`} />
              {t('publicationPolicy:pd:architecture')}
            </div>
            <h3>{t('publicationPolicy:ad:title')}</h3>
            {reactStringReplace(t('publicationPolicy:ad:line1'), '$#$',
              (m, i) => {
                if (i === 1) {
                  return (
                    <a
                      key={i}
                      className="download-file"
                      href="/api/ocds/package/all"
                    >
                      {t('publicationPolicy:ad:line1:link1')}
                    </a>
                  );
                }
                return (
                  <a
                    key={i}
                    className="download-file"
                    href="/swagger-ui/#/ocds-controller"
                  >
                    {t('publicationPolicy:ad:line1:link2')}
                  </a>
                );
              })}
            <p />
            {reactStringReplace(t('publicationPolicy:ad:line2'), '$#$',
              (m, i) => (
                <a
                  key={i}
                  className="download-file"
                  href="/"
                >
                  web address
                </a>
              ))}
            <p />
            {t('publicationPolicy:ad:line3')}
            <p />
            {t('publicationPolicy:ad:line4')}

            <h3>{t('publicationPolicy:dataScope:title')}</h3>

            <ul>
              <li>
                <b>{t('publicationPolicy:dataScope:line1:label')}</b>
                {t('publicationPolicy:dataScope:line1:text')}
              </li>
              <li>
                <b>{t('publicationPolicy:dataScope:line2:label')}</b>
                {t('publicationPolicy:dataScope:line2:text')}
              </li>
              <li>
                <b>{t('publicationPolicy:dataScope:line3:label')}</b>
                {t('publicationPolicy:dataScope:line3:text')}
              </li>
              <li>
                <b>{t('publicationPolicy:dataScope:line4:label')}</b>
                {t('publicationPolicy:dataScope:line4:text')}
              </li>
              <li>
                <b>{t('publicationPolicy:dataScope:line5:label')}</b>
                {t('publicationPolicy:dataScope:line5:text')}
              </li>
              <li>
                <b>{t('publicationPolicy:dataScope:line6:label')}</b>
                {t('publicationPolicy:dataScope:line6:text')}
              </li>
            </ul>

            <h3>{t('publicationPolicy:licensing:title')}</h3>
            {reactStringReplace(t('publicationPolicy:licensing:text'), '$#$',
              (m, i) => <a key={i} className="download-file" href="https://creativecommons.org/licenses/by-sa/4.0/">{t('publicationPolicy:licensing:text:link')}</a>)}

            <h3>{t('publicationPolicy:devPlans:title')}</h3>
            {t('publicationPolicy:devPlans:text')}

            <h3>{t('publicationPolicy:pubContact:title')}</h3>
            {reactStringReplace(t('publicationPolicy:pubContact:text'), '$#$',
              (m, i) => <a key={i} className="download-file" href="mailto:info@nandi.go.ke">info@nandi.go.ke</a>)}

            <h3>{t('publicationPolicy:disclaimer:title')}</h3>
            {t('publicationPolicy:disclaimer:text')}

            <h3>{t('publicationPolicy:dataAndExceptions:title')}</h3>
            {t('publicationPolicy:dataAndExceptions:text')}
            <p />

            <a
              className="download-file"
              href="https://github.com/devgateway/forms-makueni/tree/master-elgeyo/persistence-mongodb/src/main/resources/extensions/contract_contractor"
            >
              {t('publicationPolicy:dataAndExceptions:contractContractor')}
            </a>
            <p />

            <a
              className="download-file"
              href="https://github.com/devgateway/forms-makueni/tree/master-elgeyo/persistence-mongodb/src/main/resources/extensions/first_time_winners"
            >
              {t('publicationPolicy:dataAndExceptions:firstTimeWinners')}
            </a>
            <p />
            <a
              className="download-file"
              href="https://github.com/devgateway/forms-makueni/tree/master-elgeyo/persistence-mongodb/src/main/resources/extensions/fiscal_year"
            >
              {t('publicationPolicy:dataAndExceptions:fiscalYear')}
            </a>
            <p />
            <a
              className="download-file"
              href="https://github.com/devgateway/forms-makueni/tree/master-elgeyo/persistence-mongodb/src/main/resources/extensions/milestone_delayed_authorization"
            >
              {t('publicationPolicy:dataAndExceptions:delayedMilestone')}
            </a>
            <p />
            <a
              className="download-file"
              href="https://github.com/devgateway/forms-makueni/tree/master-elgeyo/persistence-mongodb/src/main/resources/extensions/planning_items"
            >
              {t('publicationPolicy:dataAndExceptions:plannedItems')}
            </a>
            <p />
            <a
              className="download-file"
              href="https://github.com/devgateway/forms-makueni/tree/master-elgeyo/persistence-mongodb/src/main/resources/extensions/target_groups"
            >
              {t('publicationPolicy:dataAndExceptions:targetGroups')}
            </a>
            <p />
            <a
              className="download-file"
              href="https://github.com/devgateway/forms-makueni/tree/master-elgeyo/persistence-mongodb/src/main/resources/extensions/tender_location"
            >
              {t('publicationPolicy:dataAndExceptions:tenderLocation')}
            </a>
            <p />
            <a
              className="download-file"
              href="https://github.com/open-contracting-extensions/ocds_bid_extension"
            >
              {t('publicationPolicy:dataAndExceptions:bidExtension')}
            </a>
            <p />
            <a
              className="download-file"
              href="https://github.com/open-contracting-extensions/ocds_budget_breakdown_extension"
            >
              {t('publicationPolicy:dataAndExceptions:budgetBreakdownExtensions')}
            </a>
            <p />
            <a
              className="download-file"
              href=" https://github.com/open-contracting-extensions/ocds_location_extension"
            >
              {t('publicationPolicy:dataAndExceptions:locationExtension')}
            </a>

            <h3>{t('publicationPolicy:ocdsMapping:title')}</h3>
            {reactStringReplace(t('publicationPolicy:ocdsMapping:text'), '$#$',
              (m, i) => {
                if (i === 1) {
                  return <a key={i} className="download-file" href={`${process.env.PUBLIC_URL}/OCDS-Conversion-Matrix.xlsx`}>{t('publicationPolicy:ocdsMapping:text:link1')}</a>;
                }
                return <a key={i} target="_blank" className="download-file" href={`${process.env.PUBLIC_URL}/javadoc/index.html`}>{t('publicationPolicy:ocdsMapping:text:link2')}</a>;
              })}
          </div>

        </div>
      </div>
    );
  }
}

export default Docs;
