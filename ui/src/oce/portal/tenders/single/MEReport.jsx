import React from 'react';
import { useTranslation } from 'react-i18next';
import ImplReport from './ImplReport';
import fmConnect from '../../../fm/fm';
import { Item } from './Item';
import defaultSingleTenderTabTypes from './singleUtil';

const MEReport = (props) => {
  const { t } = useTranslation();

  const childElements = (i) => {
    const { currencyFormatter, formatDate, formatBoolean } = props.styling.tables;
    const { isFeatureVisible } = props;
    return (
      <div>
        <div className="row display-flex">
          {isFeatureVisible('publicView.meReport.sno')
        && <Item label={t('meReport:sno')} value={i.sno} col={3} />}

          {isFeatureVisible('publicView.meReport.subcounties')
        && (
        <Item
          label={t('meReport:subcounties')}
          value={i.subcounties.map((item) => item.label).join(', ')}
          col={3}
        />
        )}

          {isFeatureVisible('publicView.meReport.wards')
        && <Item label={t('meReport:wards')} value={i.wards.map((item) => item.label).join(', ')} col={3} />}

          {isFeatureVisible('publicView.meReport.subwards')
        && (
        <Item
          label={t('meReport:subwards')}
          value={i.subwards && i.subwards.map((item) => item.label).join(', ')}
          col={3}
        />
        )}

          {isFeatureVisible('publicView.meReport.lpoAmount')
        && <Item label={t('meReport:lpoAmount')} value={currencyFormatter(i.lpoAmount)} col={3} />}

          {isFeatureVisible('publicView.meReport.lpoNumber')
        && <Item label={t('meReport:lpoNumber')} value={i.lpoNumber} col={3} />}

          {isFeatureVisible('publicView.meReport.expenditure')
        && <Item label={t('meReport:expenditure')} value={currencyFormatter(i.expenditure)} col={3} />}

          {isFeatureVisible('publicView.meReport.uncommitted')
        && <Item label={t('meReport:uncommitted')} value={currencyFormatter(i.uncommitted)} col={3} />}

          {isFeatureVisible('publicView.meReport.projectScope')
        && <Item label={t('meReport:projectScope')} value={i.projectScope} col={3} />}

          {isFeatureVisible('publicView.meReport.output')
        && <Item label={t('meReport:output')} value={i.output} col={3} />}

          {isFeatureVisible('publicView.meReport.outcome')
        && <Item label={t('meReport:outcome')} value={i.outcome} col={3} />}

          {isFeatureVisible('publicView.meReport.projectProgress')
        && <Item label={t('meReport:projectProgress')} value={i.projectProgress} col={3} />}

          {isFeatureVisible('publicView.meReport.directBeneficiariesTarget')
        && <Item label={t('meReport:directBeneficiariesTarget')} value={i.directBeneficiariesTarget} col={3} />}

          {isFeatureVisible('publicView.meReport.wayForward')
        && <Item label={t('meReport:wayForward')} value={i.wayForward} col={3} />}

          {isFeatureVisible('publicView.meReport.byWhen')
        && <Item label={t('meReport:byWhen')} value={formatDate(i.byWhen)} col={3} />}

          {isFeatureVisible('publicView.meReport.inspected')
        && <Item label={t('meReport:inspected')} value={formatBoolean(i.inspected)} col={3} />}

          {isFeatureVisible('publicView.meReport.invoiced')
        && <Item label={t('meReport:invoiced')} value={formatBoolean(i.invoiced)} col={3} />}

          {isFeatureVisible('publicView.meReport.officerResponsible')
        && <Item label={t('meReport:officerResponsible')} value={i.officerResponsible} col={3} />}

          {isFeatureVisible('publicView.meReport.meStatus')
        && <Item label={t('meReport:meStatus')} value={i.meStatus.label} col={3} />}

          {isFeatureVisible('publicView.meReport.remarks')
        && <Item label={t('meReport:remarks')} value={i.remarks} col={3} />}

          {isFeatureVisible('publicView.meReport.contractorContact')
        && <Item label={t('meReport:contractorContact')} value={i.contractorContact} col={3} />}

          {isFeatureVisible('publicView.meReport.contractDate')
        && <Item label={t('meReport:contractDate')} value={formatDate(i.contract.contractDate)} col={3} />}

          {isFeatureVisible('publicView.meReport.contractExpiryDate')
        && <Item label={t('meReport:expiryDate')} value={formatDate(i.contract.expiryDate)} col={3} />}

          {isFeatureVisible('publicView.meReport.amountBudgeted')
        && <Item label={t('meReport:amountBudgeted')} value={currencyFormatter(i.amountBudgeted)} col={3} />}
        </div>
      </div>
    );
  };

  return (
    <ImplReport
      {...props}
      fmPrefix="publicView.meReport"
      reportName={t('meReport:label')}
      childElements={childElements}
    />
  );
};

MEReport.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(MEReport);
