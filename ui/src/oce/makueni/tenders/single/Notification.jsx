import React from 'react';
import NoDataMessage from './NoData';
import fmConnect from '../../../fm/fm';
import { Item } from './Item';
import FileDownloadLinks from './FileDownloadLinks';
import { tCreator } from '../../../translatable';
import defaultSingleTenderTabTypes from './singleUtil';

const Notification = (props) => {
  const t = tCreator(props.translations);
  const { data, isFeatureVisible } = props;
  const { currencyFormatter, formatDate } = props.styling.tables;

  // eslint-disable-next-line no-unused-vars
  const getFeedbackSubject = () => {
    const { tenderTitle, department, fiscalYear } = props;
    let metadata;
    if (department !== undefined) {
      metadata = ` - ${tenderTitle
      } - ${department.label
      } - ${fiscalYear.name}`;
    }
    return escape(t('notification:subject') + metadata);
  };

  const getNotification = (awardNotification) => (
    <div>
      <div className="padding-top-10">
        {
          awardNotification.items !== undefined && isFeatureVisible('publicView.awardNotification.items')
            ? awardNotification.items.map((i) => (
              <div key={i._id} className="box">
                <div className="row">
                  {isFeatureVisible('publicView.awardNotification.items.awardValue')
                  && <Item label={t('notification:awardValue')} value={currencyFormatter(i.awardValue)} col={4} />}

                  {isFeatureVisible('publicView.awardNotification.items.tenderAwardDate')
                  && <Item label={t('notification:tenderAwardDate')} value={formatDate(i.tenderAwardDate)} col={4} />}

                  {isFeatureVisible('publicView.awardNotification.items.awardDate')
                  && <Item label={t('notification:awardDate')} value={formatDate(i.awardDate)} col={4} />}

                  {isFeatureVisible('publicView.awardNotification.items.acknowledgementDays')
                  && <Item label={t('notification:acknowledgementDays')} value={i.acknowledgementDays} col={4} />}

                  {isFeatureVisible('publicView.awardNotification.items.awardee.label')
                  && <Item label={t('notification:awardee:label')} value={i.awardee.label} col={4} />}

                  {isFeatureVisible('publicView.awardNotification.items.awardee.code')
                  && <Item label={t('notification:awardee:code')} value={i.awardee.code} col={4} />}

                  {isFeatureVisible('publicView.awardNotification.items.awardee.address')
                  && <Item label={t('notification:awardee:address')} value={i.awardee.address} col={4} />}

                  {isFeatureVisible('publicView.awardNotification.items.formDocs')
                  && (
                  <Item label={t('notification:docs')} col={12}>
                    <FileDownloadLinks files={i.formDocs} useDash />
                  </Item>
                  )}
                </div>

              </div>
            )) : null
        }
      </div>
    </div>
  );

  return (data === undefined ? <NoDataMessage translations={props.translations} /> : getNotification(data[0]));
};

Notification.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(Notification);
