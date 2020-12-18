import React, {useEffect} from "react";
import {Item} from "../../tenders/single/Item";
import FileDownloadLinks from "../../tenders/single/FileDownloadLinks";
import fmConnect from "../../../fm/fm";
import {tCreator} from "../../../translatable";
import {getProcurementPlan} from "../../../api/Api";
import PropTypes from "prop-types";
import {useImmer} from "use-immer";
import {setImmer} from "../../../tools";

const ProcurementPlan = props => {

  const { id, navigate, translations, isFeatureVisible } = props;

  const t = tCreator(translations);

  const [data, updateData] = useImmer(undefined);

  const getFeedbackSubject = () => {
    let metadata;
    if (data !== undefined) {
      metadata = " - " + data.department.label + " - " + data.fiscalYear.name;
    }
    return escape(t("procurementPlan:feedbackSubject") + metadata);
  }

  const getFeedbackMessage = () => {
    return (<div className="row feedback-section" >
      <a href={"mailto:opencontracting@makueni.go.ke?Subject=" + getFeedbackSubject()} target="_top">
        <div className="col-md-offset-8 col-md-4" data-intro={t("feedbackPage:link:dataIntro")}>
          <div className="pull-right">
            <span>{t("feedbackPage:link:label")}</span>
            <img className="feedback-icon" src={process.env.PUBLIC_URL + "/icons/feedback.svg"} alt="feedback"/>
          </div>
        </div>
      </a>
    </div>);
  }

  useEffect(() => {
    getProcurementPlan(id).then(setImmer(updateData));
  }, [id]);

  const { currencyFormatter, formatDate } = props.styling.tables;

  return (<div className="procurement-plan makueni-form">
    <div className="row">
      <a href="#!/procurement-plan" onClick={() => navigate()} className="back-link col-md-3">
        <span className="back-icon">
          <span className="previous">&#8249;</span>
        </span>
        <span className="back-text">
            {t("general:goBack")}
        </span>
      </a>
    </div>

    <div className="row padding-top-10">
      <div className="col-md-12">
        <h1 className="page-title">{t("procurementPlan:title")}</h1>
      </div>
    </div>

    {
      data !== undefined
        ? <div>
          <div className="row">
            {isFeatureVisible("publicView.procurementPlan.department")
            && <Item label={t("procurementPlan:department")} value={data.department.label} col={8} />}
            {isFeatureVisible("publicView.procurementPlan.fiscalYear")
            && <Item label={t("procurementPlan:fiscalYear")} value={data.fiscalYear.name} col={4} />}
          </div>

          {
            data.planItems !== undefined && isFeatureVisible("publicView.procurementPlan.planItems")
              ? <div>
                <div className="row padding-top-10">
                  <div className="col-md-12 sub-title">
                    {t("procurementPlan:item:caption").replace("$#$", data.planItems.length)}
                  </div>
                </div>

                {
                  data.planItems.map(planItem => <div key={planItem.id} className="box">
                    {planItem.item && isFeatureVisible("publicView.procurementPlan.planItems.label")
                    && <div className="row">
                      <Item label={t("procurementPlan:item:item")} value={planItem.item.label} col={12} />
                    </div>}

                    <div className="row display-flex">
                      {planItem.unitOfIssue && isFeatureVisible("publicView.procurementPlan.planItems.unitOfIssue")
                      && <Item label={t("procurementPlan:item:unitOfIssue")}
                               value={planItem.unitOfIssue.label} col={3} />}
                      {isFeatureVisible("publicView.procurementPlan.planItems.quantity")
                      && <Item label={t("procurementPlan:item:quantity")}
                               value={currencyFormatter(planItem.quantity)} col={3} />}
                      {isFeatureVisible("publicView.procurementPlan.planItems.estimatedCost")
                      && <Item label={t("procurementPlan:item:estimatedCost")}
                               value={currencyFormatter(planItem.estimatedCost)} col={3} />}
                      {planItem.quantity && planItem.estimatedCost && isFeatureVisible("publicView.procurementPlan.planItems.totalCost")
                      && <Item label={t("procurementPlan:item:totalCost")}
                               value={currencyFormatter(planItem.quantity * planItem.estimatedCost)} col={3} />}
                      {planItem.procurementMethod && isFeatureVisible("publicView.procurementPlan.planItems.procurementMethod")
                      && <Item label={t("procurementPlan:item:procurementMethod")}
                               value={planItem.procurementMethod.label} col={3} />}
                      {isFeatureVisible("publicView.procurementPlan.planItems.sourceOfFunds")
                      && <Item label={t("procurementPlan:item:sourceOfFunds")}
                               value={planItem.sourceOfFunds} col={3} />}
                      {planItem.targetGroup && isFeatureVisible("publicView.procurementPlan.planItems.targetGroup")
                      && <Item label={t("procurementPlan:item:targetGroup")}
                               value={(planItem.targetGroup || {}).label} col={3} />}
                      {isFeatureVisible("publicView.procurementPlan.planItems.targetGroupValue")
                      && <Item label={t("procurementPlan:item:targetGroupValue")}
                               value={currencyFormatter(planItem.targetGroupValue)} col={3} />}
                    </div>

                    <div className="row">
                      <h4 className="col-md-12">{t("procurementPlan:item:timingOfActivities")}</h4>
                      {isFeatureVisible("publicView.procurementPlan.planItems.quarter1st")
                      && <Item label={t("procurementPlan:item:quarter1st")}
                               value={currencyFormatter(planItem.quarter1st)} col={3} />}
                      {isFeatureVisible("publicView.procurementPlan.planItems.quarter2nd")
                      && <Item label={t("procurementPlan:item:quarter2nd")}
                               value={currencyFormatter(planItem.quarter2nd)} col={3} />}
                      {isFeatureVisible("publicView.procurementPlan.planItems.quarter3rd")
                      && <Item label={t("procurementPlan:item:quarter3rd")}
                               value={currencyFormatter(planItem.quarter3rd)} col={3} />}
                      {isFeatureVisible("publicView.procurementPlan.planItems.quarter4th")
                      && <Item label={t("procurementPlan:item:quarter4th")}
                               value={currencyFormatter(planItem.quarter4th)} col={3} />}
                    </div>
                  </div>)
                }

                <div className="row">
                  {isFeatureVisible("publicView.procurementPlan.formDocs")
                  && <Item label={t("procurementPlan:item:ppDocs")} col={6}>
                    <FileDownloadLinks files={data.formDocs || []} useDash />
                  </Item>}
                  {isFeatureVisible("publicView.procurementPlan.approvedDate")
                  && <Item label={t("procurementPlan:item:approvedDate")} value={formatDate(data.approvedDate)} col={6} />}
                </div>
              </div>
              : null
          }
        </div>
        : null
    }

    {getFeedbackMessage()}
  </div>);
}

ProcurementPlan.propTypes = {
  id: PropTypes.string.isRequired,
  navigate: PropTypes.func.isRequired,
  styling: PropTypes.object.isRequired
};

export default fmConnect(ProcurementPlan);
