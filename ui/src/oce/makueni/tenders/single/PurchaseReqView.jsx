import cn from 'classnames';
import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { useImmer } from 'use-immer';
import { useTranslation } from 'react-i18next';
import { Link, useParams, useHistory } from 'react-router-dom';
import Tender from './Tender';
import PurchaseReq from './PurchaseReq';
import TenderQuotation from './TenderQuotation';
import ProfessionalOpinion from './ProfessionalOpinion';
import Notification from './Notification';
import Award from './Award';
import Contract from './Contract';
import FeedbackMessageList from '../../../feedback/feedbackList';
import AdministratorReport from './AdministratorReport';
import fmConnect from '../../../fm/fm';
import { getPurchaseRequisition } from '../../../api/Api';
import InspectionReport from './InspectionReport';
import PMCReport from './PMCReport';
import MEReport from './MEReport';
import PaymentVoucher from './PaymentVoucher';
import { setImmer } from '../../../tools';

const PurchaseReqView = (props) => {
  const [visibleTabs, setVisibleTabs] = useState([]);

  const [data, updateData] = useImmer(undefined);
  const { isFeatureVisible } = props;
  const { id, selected = '1' } = useParams();
  const history = useHistory();
  const { t } = useTranslation();

  const maybeTrimOcidPrefix = (id) => {
    if (id.includes('ocds-muq5cl-')) {
      return id.replace('ocds-muq5cl-', '');
    }
    return id;
  };

  const tabs = [
    {
      name: t('purchaseReq:tabs:tender'),
      tab: '1',
      fm: 'publicView.tender',
      dataFn: (data) => data.tenderProcesses.tender,
      component: Tender,
    }, {
      name: t('purchaseReq:tabs:purchaseReqs'),
      tab: '2',
      fm: 'publicView.purchaseRequisition',
      dataFn: (data) => data.tenderProcesses.purchaseRequisition,
      component: PurchaseReq,
    }, {
      name: t('purchaseReq:tabs:tenderEvaluation'),
      tab: '3',
      fm: 'publicView.tenderQuotationEvaluation',
      dataFn: (data) => data.tenderProcesses.tenderQuotationEvaluation,
      component: TenderQuotation,
    }, {
      name: t('purchaseReq:tabs:professionalOpinion'),
      tab: '4',
      fm: 'publicView.professionalOpinions',
      dataFn: (data) => data.tenderProcesses.professionalOpinion,
      component: ProfessionalOpinion,
    }, {
      name: t('purchaseReq:tabs:notification'),
      tab: '5',
      fm: 'publicView.awardNotification',
      dataFn: (data) => data.tenderProcesses.awardNotification,
      component: Notification,
    }, {
      name: t('purchaseReq:tabs:award'),
      tab: '6',
      fm: 'publicView.awardAcceptance',
      dataFn: (data) => data.tenderProcesses.awardAcceptance,
      component: Award,
    }, {
      name: t('purchaseReq:tabs:contract'),
      tab: '7',
      fm: 'publicView.contract',
      dataFn: (data) => data.tenderProcesses.contract,
      component: Contract,
    }, {
      name: t('purchaseReq:tabs:adminReports'),
      tab: '8',
      fm: 'publicView.administratorReport',
      dataFn: (data) => data.tenderProcesses.administratorReports,
      component: AdministratorReport,
    }, {
      name: t('purchaseReq:tabs:inspectionReports'),
      tab: '9',
      fm: 'publicView.inspectionReport',
      dataFn: (data) => data.tenderProcesses.inspectionReports,
      component: InspectionReport,
    }, {
      name: t('purchaseReq:tabs:pmcReports'),
      tab: '10',
      fm: 'publicView.pmcReport',
      dataFn: (data) => data.tenderProcesses.pmcReports,
      component: PMCReport,
    }, {
      name: t('purchaseReq:tabs:meReports'),
      tab: '11',
      fm: 'publicView.meReport',
      dataFn: (data) => data.tenderProcesses.meReports,
      component: MEReport,
    }, {
      name: t('purchaseReq:tabs:paymentVouchers'),
      tab: '12',
      fm: 'publicView.paymentVoucher',
      dataFn: (data) => data.tenderProcesses.paymentVouchers,
      component: PaymentVoucher,
    },
  ];

  useEffect(() => {
    getPurchaseRequisition({ id: maybeTrimOcidPrefix(id) })
      .then(setImmer(updateData));
  }, [id]);

  useEffect(() => {
    if (!data) {
      return;
    }
    setVisibleTabs(tabs.filter((tab) => isFeatureVisible(tab.fm) && tab.dataFn(data)));
  }, [data]);

  const isActive = (option) => {
    if (selected === undefined || selected === '') {
      return false;
    }
    return selected === option;
  };

  const displayTab = () => {
    const { tenderTitle } = data.tenderProcesses.tender[0];

    return visibleTabs
      .filter((currentTab) => currentTab.tab === selected)
      .map((currentTab) => (
        <currentTab.component
          key={currentTab.tab}
          data={currentTab.dataFn(data)}
          department={data.department}
          fiscalYear={data.fiscalYear}
          styling={props.styling}
          t={t}
          prId={data.tenderProcesses._id}
          tenderTitle={tenderTitle}
        />
      ));
  };

  return (data !== undefined && (
  <div className="tender makueni-form">
    <div className="row">
      <div className="col-md-12 navigation-view" data-step="9" data-intro={t('purchaseReq:nav:dataIntro')}>
        {
          visibleTabs.map((tab) => (
            <Link
              to={`/ui/tender/t/${id}/${tab.tab}`}
              key={tab.tab}
              className={cn('', { active: isActive(tab.tab) })}
            >
              {tab.name}
            </Link>
          ))
        }
      </div>
    </div>

    <div className="row">
      <a href="#" onClick={() => history.goBack()} className="back-link col-md-3">
        <span className="back-icon">
          <span className="previous">&#8249;</span>
        </span>
        <span>
          {t('general:goBack')}
        </span>
      </a>

      {isFeatureVisible('publicView.tender.receiveAlertsButton') && (
      <div className="col-md-offset-5 col-md-4">
        <button
          className="btn btn-subscribe pull-right"
          type="submit"
          data-step="11"
          data-intro={t('purchaseReq:receiveAlerts:dataIntro')}
          onClick={() => props.onSwitch('alerts', data.tenderProcesses._id, data.tenderProcesses.tender[0].tenderTitle)}
        >
          {t('purchaseReq:receiveAlerts:caption')}
        </button>
      </div>
      )}
    </div>
    {
      displayTab()
    }

    <div className="row">
      <p />
      <FeedbackMessageList department={data.department} />
    </div>
  </div>
  ));
};

PurchaseReqView.propTypes = {
  isFeatureVisible: PropTypes.func.isRequired,
  styling: PropTypes.object.isRequired,
};

export default fmConnect(PurchaseReqView);
