import React from 'react';
import NoDataMessage from './NoData';
import fmConnect from '../../../fm/fm';
import { Item } from './Item';
import FileDownloadLinks from './FileDownloadLinks';
import { tCreator } from '../../../translatable';
import defaultSingleTenderTabTypes from './singleUtil';

const Contract = (props) => {
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
    return escape(t('contract:subject') + metadata);
  };

  const getContract = (contract) => (
    <div>
      <div className="row display-flex">
        {isFeatureVisible('publicView.contract.referenceNumber')
        && <Item label={t('contract:referenceNumber')} value={contract.referenceNumber} col={4} />}

        {isFeatureVisible('publicView.contract.contractDate')
        && <Item label={t('contract:contractDate')} value={formatDate(contract.contractDate)} col={4} />}

        {isFeatureVisible('publicView.contract.expiryDate')
        && <Item label={t('contract:expiryDate')} value={formatDate(contract.expiryDate)} col={4} />}

        {isFeatureVisible('publicView.contract.contractExtensionDate')
        && <Item label={t('contract:contractExtensionDate')} value={formatDate(contract.contractExtensionDate)} col={4} />}

        {isFeatureVisible('publicView.contract.reasonForExtension')
        && <Item label={t('contract:reasonForExtension')} value={contract.reasonForExtension} col={4} />}

        {isFeatureVisible('publicView.contract.awardee.label')
        && <Item label={t('contract:awardee:label')} value={contract.awardee.label} col={4} />}

        {isFeatureVisible('publicView.contract.awardee.address')
        && <Item label={t('contract:awardee:address')} value={contract.awardee.address} col={4} />}

        {isFeatureVisible('publicView.contract.targetGroup')
        && <Item label={t('contract:targetGroup')} value={contract.targetGroup.label} col={4} />}

        {isFeatureVisible('publicView.contract.contractValue')
        && <Item label={t('contract:contractValue')} value={currencyFormatter(contract.contractValue)} col={4} />}

        {isFeatureVisible('publicView.contract.contractApprovalDate')
        && (
        <Item
          label={t('contract:contractApprovalDate')}
          value={formatDate(contract.contractApprovalDate)}
          col={4}
        />
        )}

        {isFeatureVisible('publicView.contract.description')
        && (
          <Item
            label={t('contract:description')}
            value={contract.description}
            col={12}
          />
        )}

        {isFeatureVisible('publicView.contract.wards')
        && (
        <Item
          label={t('contract:wards')}
          value={contract.wards && contract.wards.map((item) => item.label).join(', ')}
          col={4}
        />
        )}

        {isFeatureVisible('publicView.contract.subcounties')
        && (
        <Item
          label={t('contract:subcounties')}
          value={contract.subcounties && contract.subcounties.map((item) => item.label).join(', ')}
          col={4}
        />
        )}
      </div>

      {
        contract.contractDocs !== undefined && isFeatureVisible('publicView.contract.contractDocs')
          ? (
            <div>
              <div className="row padding-top-10">
                <div className="col-md-12 sub-title">
                  {t('contract:contractDocs')}
                  (
                  {contract.contractDocs.length}
                  )
                </div>
              </div>

              {
                contract.contractDocs.map((contractDoc) => (
                  <div key={contractDoc._id} className="box">
                    <div className="row">
                      {isFeatureVisible('publicView.contract.contractDocs.contractDocumentType')
                    && (
                    <Item
                      label={t('contract:contractDocs:type')}
                      value={contractDoc.contractDocumentType.label}
                      col={6}
                    />
                    )}

                      {isFeatureVisible('publicView.contract.contractDocs.formDocs')
                    && (
                    <Item label={t('contract:contractDocs')} col={6}>
                      <FileDownloadLinks files={contractDoc.formDocs} useDash />
                    </Item>
                    )}
                    </div>
                  </div>
                ))
              }
            </div>
          )
          : null
      }
    </div>
  );

  return (data === undefined ? <NoDataMessage translations={props.translations} /> : getContract(data[0]));
};

Contract.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(Contract);
