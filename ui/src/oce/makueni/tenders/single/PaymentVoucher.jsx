import React from 'react';
import { useTranslation } from 'react-i18next';
import fmConnect from '../../../fm/fm';
import { Item } from './Item';
import ImplReport from './ImplReport';
import defaultSingleTenderTabTypes from './singleUtil';
import FileDownloadLinks from './FileDownloadLinks';

const getPaymentStatus = (paymentVouchers) => {
  if (paymentVouchers.find((el) => el.status === 'APPROVED' && el.lastPayment)) {
    return 'paymentVoucher:paymentStatus:paidInFull';
  }
  if (paymentVouchers.find((el) => el.status === 'APPROVED')) {
    return 'paymentVoucher:paymentStatus:partiallyPaid';
  }
  return 'paymentVoucher:paymentStatus:nonePaid';
};

const PaymentVoucher = (props) => {
  const { isFeatureVisible, data } = props;
  const { t } = useTranslation();

  const childElements = (i) => {
    const { currencyFormatter, formatBoolean } = props.styling.tables;
    return (
      <div>
        <div className="row display-flex">
          {isFeatureVisible('publicView.paymentVoucher.pmcReport')
        && <Item label={t('paymentVoucher:pmcReport')} value={i.pmcReport.label} col={3} />}

          {isFeatureVisible('publicView.paymentVoucher.inspectionReport')
        && <Item label={t('paymentVoucher:inspectionReport')} value={i.inspectionReport.label} col={3} />}

          {isFeatureVisible('publicView.paymentVoucher.administratorReport')
        && <Item label={t('paymentVoucher:administratorReport')} value={i.administratorReport.label} col={3} />}

          {isFeatureVisible('publicView.paymentVoucher.totalAmount')
        && <Item label={t('paymentVoucher:totalAmount')} value={currencyFormatter(i.totalAmount)} col={3} />}

          {isFeatureVisible('publicView.paymentVoucher.lastPayment')
        && <Item label={t('paymentVoucher:lastPayment')} value={formatBoolean(i.lastPayment)} col={3} />}

          {isFeatureVisible('publicView.paymentVoucher.contractRefNum')
        && (
        <Item
          label={t('paymentVoucher:contractNumber')}
          value={currencyFormatter(i.contract.referenceNumber)}
          col={3}
        />
        )}

          {isFeatureVisible('publicView.paymentVoucher.completionCertificate')
          && (
            <Item label={t('paymentVoucher:completionCertificate')} col={12}>
              <FileDownloadLinks files={i.completionCertificate} useDash />
            </Item>
          )}
        </div>
      </div>
    );
  };

  const header = (
    <>
      {isFeatureVisible('publicView.paymentVoucher.completionCertificate')
      && (
        <div className="row display-flex">
          <Item label={t('paymentVoucher:paymentStatus')} value={t(getPaymentStatus(data))} col={3} />
        </div>
      )}
    </>
  );

  return (
    <ImplReport
      {...props}
      fmPrefix="publicView.paymentVoucher"
      reportName={t('paymentVoucher:label')}
      childElements={childElements}
      header={header}
    />
  );
};

PaymentVoucher.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(PaymentVoucher);
