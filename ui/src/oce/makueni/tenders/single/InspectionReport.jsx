import React from 'react';
import moment from 'moment';
import { useTranslation } from 'react-i18next';
import fmConnect from '../../../fm/fm';
import { Item } from './Item';
import FileDownloadLinks from './FileDownloadLinks';
import AuthImplReport from './AuthImplReport';
import defaultSingleTenderTabTypes from './singleUtil';

const InspectionReport = (props) => {
  const { t } = useTranslation();

  const authChildren = (i) => {
    const { currencyFormatter } = props.styling.tables;
    const { isFeatureVisible } = props;
    return (
      <div>
        {isFeatureVisible('publicView.inspectionReport.contractValue')
          && (
          <Item
            label={t('inspectionReport:contractValue')}
            value={currencyFormatter(i.contract.contractValue)}
            col={3}
          />
          )}
      </div>
    );
  };

  const getNearestRequestDate = (i) => {
    const reqDate = moment(i.privateSectorRequests.map((item) => new Date(item.requestDate)).sort()[0]);
    const approvedDate = moment(i.approvedDate);

    return reqDate.diff(approvedDate, 'days');
  };

  const childElements = (i) => {
    const { formatDate } = props.styling.tables;
    const { isFeatureVisible } = props;
    return (
      <div key="2">
        <div className="row">
          {isFeatureVisible('publicView.inspectionReport.comments')
        && <Item label={t('inspectionReport:comments')} value={i.comments} col={6} />}

          {
          i.privateSectorRequests !== undefined
          && isFeatureVisible('publicView.inspectionReport.privateSectorRequests')
          && isFeatureVisible('publicView.inspectionReport.privateSectorRequests.requestDate')
            ? (
              <Item
                label={t('inspectionReport:daysUntilReport')}
                value={getNearestRequestDate(i)}
                col={6}
              />
            )
            : null
        }
        </div>
        {
          i.privateSectorRequests !== undefined && isFeatureVisible('publicView.inspectionReport.privateSectorRequests')
            ? (
              <div className="row padding-top-10">
                <div className="col-md-12 sub-title">
                  {t('inspectionReport:privateSectorRequest:pl')}
                  (
                  {i.privateSectorRequests.length}
                  )
                </div>
                {i.privateSectorRequests.map((psr) => (
                  <div key={psr._id} className="box">
                    <div className="row">
                      {isFeatureVisible('publicView.inspectionReport.privateSectorRequests.upload')
                  && (
                  <Item label={t('inspectionReport:privateSectorRequest:sg')} col={3}>
                    <FileDownloadLinks files={psr.upload} useDash />
                  </Item>
                  )}

                      {isFeatureVisible('publicView.inspectionReport.privateSectorRequests.requestDate')
                  && (
                  <Item
                    label={t('inspectionReport:privateSectorRequest:requestDate')}
                    value={formatDate(psr.requestDate)}
                    col={3}
                  />
                  )}
                    </div>
                  </div>
                ))}
              </div>
            ) : null
        }
      </div>
    );
  };

  return (
    <AuthImplReport
      {...props}
      reportName={t('inspectionReport:label')}
      fmPrefix="publicView.inspectionReport"
      authChildren={authChildren}
      childElements={childElements}
    />
  );
};

InspectionReport.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(InspectionReport);
