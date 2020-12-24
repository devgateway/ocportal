import React from 'react';
import PropTypes from 'prop-types';
import NoDataMessage from './NoData';
import fmConnect from '../../../fm/fm';
import { Item } from './Item';
import FileDownloadLinks from './FileDownloadLinks';
import { tCreator } from '../../../translatable';
import defaultSingleTenderTabTypes, { commonTenderTabTypes } from './singleUtil';

const Tender = (props) => {
  const getFeedbackSubject = () => {
    const { data, department, fiscalYear } = props;

    let metadata;
    if (data !== undefined) {
      const tender = data[0];

      metadata = ` - ${tender.tenderTitle
      } - ${department.label
      } - ${fiscalYear.name}`;
    }
    return escape(t('tender:label') + metadata);
  };

  const { data, prId, isFeatureVisible } = props;
  const { currencyFormatter, formatDate } = props.styling.tables;
  const t = tCreator(props.translations);

  const getTenderView = (tender) => (
    <div>
      <div className="row display-flex">
        {isFeatureVisible('publicView.tender.tenderTitle')
        && <Item label={t('tender:title')} value={tender.tenderTitle} col={4} />}

        {isFeatureVisible('publicView.tender.tenderNumber')
        && <Item label={t('tender:number')} value={tender.tenderNumber} col={4} />}

        {isFeatureVisible('publicView.tender.tenderCode')
        && <Item label={t('tender:code')} value={prId} col={4} />}

        {isFeatureVisible('publicView.tender.procurementMethod')
        && <Item label={t('tender:procurementMethod')} value={tender.procurementMethod.label} col={4} />}

        {isFeatureVisible('publicView.tender.procurementMethodRationale')
        && (
        <Item
          label={t('tender:procurementMethodRationale')}
          value={(tender.procurementMethodRationale || {}).label}
          col={4}
        />
        )}

        {isFeatureVisible('publicView.tender.invitationDate')
        && <Item label={t('tender:invitationDate')} value={formatDate(tender.invitationDate)} col={4} />}

        {isFeatureVisible('publicView.tender.closingDate')
        && <Item label={t('tender:closingDate')} value={formatDate(tender.closingDate)} col={4} />}

        {isFeatureVisible('publicView.tender.issuedBy.label')
        && <Item label={t('tender:issuedBy:label')} value={tender.issuedBy.label} col={4} />}

        {isFeatureVisible('publicView.tender.issuedBy.address')
        && <Item label={t('tender:issuedBy:address')} value={tender.issuedBy.address} col={4} />}

        {isFeatureVisible('publicView.tender.issuedBy.emailAddress')
        && (
        <Item label={t('tender:issuedBy:email')} col={4}>
          {tender.issuedBy.emailAddress
            ? (
              <a className="download" href={`mailto:${tender.issuedBy.emailAddress}`}>
                {tender.issuedBy.emailAddress}
              </a>
            )
            : '-'}
        </Item>
        )}

        {isFeatureVisible('publicView.tender.tenderValue')
        && <Item label={t('tender:value')} value={currencyFormatter(tender.tenderValue)} col={4} />}

        {isFeatureVisible('publicView.tender.targetGroup')
        && <Item label={t('tender:targetGroup')} value={tender.targetGroup && tender.targetGroup.label} col={4} />}

        {isFeatureVisible('publicView.tender.objective')
        && <Item label={t('tender:objective')} value={tender.objective} col={12} />}
      </div>

      {
        tender.tenderItems !== undefined && isFeatureVisible('publicView.tender.tenderItems')
          ? (
            <div>
              <div className="row padding-top-10">
                <div className="col-md-12 sub-title">
                  {t('tender:tenderItems')}
                  (
                  {tender.tenderItems.length}
                  )
                </div>
              </div>

              {
                tender.tenderItems.map((tenderItem) => (
                  <div key={tenderItem._id} className="box">
                    <div className="row display-flex">
                      {isFeatureVisible('publicView.tender.tenderItems.item')
                    && <Item label={t('tender:item')} value={tenderItem.purchaseItem.planItem.item.label} col={6} />}

                      {isFeatureVisible('publicView.tender.tenderItems.description')
                    && <Item label={t('tender:item:description')} value={tenderItem.description} col={6} />}

                      {isFeatureVisible('publicView.tender.tenderItems.purchaseItem')
                    && (
                    <Item
                      label={t('tender:item:unitOfIssue')}
value={tenderItem.purchaseItem.planItem.unitOfIssue.label}
                      col={3}
                    />
                    )}

                      {isFeatureVisible('publicView.tender.tenderItems.quantity')
                    && <Item label={t('tender:item:quantity')} value={currencyFormatter(tenderItem.quantity)} col={3} />}

                      {isFeatureVisible('publicView.tender.tenderItems.unitPrice')
                    && <Item label={t('tender:item:unitPrice')} value={currencyFormatter(tenderItem.unitPrice)} col={3} />}

                      {isFeatureVisible('publicView.tender.tenderItems.totalCost')
                    && (
                    <Item
                      label={t('tender:item:totalCost')}
                      value={currencyFormatter(tenderItem.quantity * tenderItem.unitPrice)}
                      col={3}
                    />
                    )}
                    </div>
                  </div>
                ))
              }
            </div>
          )
          : null
      }

      <div className="row">
        {isFeatureVisible('publicView.tender.formDocs')
        && (
        <Item
          label={t('tender:item:dlTender')}
          col={6}
          data-intro={t('tender:item:dlTender:dataIntro')}
        >
          <FileDownloadLinks files={tender.formDocs || []} useDash />
        </Item>
        )}

        {isFeatureVisible('publicView.tender.tenderLink')
        && (
        <Item label={t('tender:item:tenderLink')} col={6}>
          {tender.tenderLink
            ? (
              <a className="download" href={tender.tenderLink} target="_blank">
                {tender.tenderLink}
              </a>
            )
            : '-'}
        </Item>
        )}
      </div>
    </div>
  );

  return (data === undefined
    ? <NoDataMessage translations={props.translations} /> : getTenderView(data[0]));
};

Tender.propTypes = {
  ...commonTenderTabTypes,
  data: PropTypes.array,
};

export default fmConnect(Tender);
