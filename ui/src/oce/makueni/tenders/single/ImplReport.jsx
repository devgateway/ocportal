import React from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import NoDataMessage from './NoData';
import { Item } from './Item';
import FileDownloadLinks from './FileDownloadLinks';
import defaultSingleTenderTabTypes from './singleUtil';

const ImplReport = (props) => {
  const {
    department, reportName, fmPrefix, childElements, header,
  } = props;
  const {
    fiscalYear, data, tenderTitle, isFeatureVisible,
  } = props;
  const { t } = useTranslation();
  const { formatDate } = props.styling.tables;

  // eslint-disable-next-line no-unused-vars
  const getFeedbackSubject = () => {
    let metadata;
    if (department !== undefined) {
      metadata = ` - ${tenderTitle
      } - ${department.label
      } - ${fiscalYear.label}`;
    }
    return escape(reportName + metadata);
  };

  const getImplReport = () => (
    <div>
      <div className="padding-top-10">
        {header}
        {
          data.slice().sort((a, b) => new Date(a.approvedDate) - new Date(b.approvedDate))
            .map((i) => (
              <div key={i._id} className="box">
                <div className="row">
                  {isFeatureVisible(`${fmPrefix}.tenderTitle`)
                  && <Item label={t('report:tenderTitle')} value={tenderTitle} col={3} />}

                  {isFeatureVisible(`${fmPrefix}.contractor`)
                  && <Item label={t('report:awardee:label')} value={i.contract.awardee.label} col={3} />}

                  {isFeatureVisible(`${fmPrefix}.fiscalYear`)
                  && <Item label={t('report:fiscalYear')} value={fiscalYear.label} col={3} />}

                  {isFeatureVisible(`${fmPrefix}.approvedDate`)
                  && <Item label={t('report:approvedDate')} value={formatDate(i.approvedDate)} col={3} />}
                </div>
                {
                  childElements(i)
                }
                {
                  i.formDocs && isFeatureVisible(`${fmPrefix}.formDocs`)
                    ? (
                      <div className="row">
                        <Item label={t('report:uploads').replace('$#$', reportName)} col={12}>
                          <FileDownloadLinks files={i.formDocs} useDash />
                        </Item>
                      </div>
                    )
                    : null
                }
              </div>
            ))
        }
      </div>
    </div>
  );

  return data === undefined ? <NoDataMessage /> : getImplReport();
};

ImplReport.propTypes = {
  ...defaultSingleTenderTabTypes,
  reportName: PropTypes.string.isRequired,
  fmPrefix: PropTypes.string.isRequired,
  childElements: PropTypes.func.isRequired,
};

export default ImplReport;
