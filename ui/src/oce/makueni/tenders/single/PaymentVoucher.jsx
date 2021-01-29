import React from 'react';
import { useTranslation } from 'react-i18next';
import fmConnect from '../../../fm/fm';
import { Item } from './Item';
import ImplReport from './ImplReport';
import defaultSingleTenderTabTypes from './singleUtil';

const PaymentVoucher = (props) => {
  const { t } = useTranslation();

  const childElements = (i) => {
    const { currencyFormatter, formatBoolean } = props.styling.tables;
    const { isFeatureVisible } = props;
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
        </div>
      </div>
    );
  };

  return (
    <ImplReport
      {...props}
      fmPrefix="publicView.paymentVoucher"
      reportName={t('paymentVoucher:label')}
      childElements={childElements}
    />
  );
};

PaymentVoucher.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(PaymentVoucher);
