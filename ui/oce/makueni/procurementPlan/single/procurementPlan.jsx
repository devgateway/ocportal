import React from "react";
import { ppState } from '../state';
import { API_ROOT } from '../../../state/oce-state';
import FeedbackPage from '../../FeedbackPage';
import {Item} from "../../tenders/single/Item";
import FileDownloadLinks from "../../tenders/single/FileDownloadLinks";
import fmConnect from "../../../fm/fm";

class ProcurementPlan extends FeedbackPage {
  constructor(props) {
    super(props);

    this.state = {};

    this.ppID = ppState.input({
      name: 'ppID',
    });

    this.ppInfoUrl = ppState.mapping({
      name: 'ppInfoUrl',
      deps: [this.ppID],
      mapper: id => `${API_ROOT}/makueni/procurementPlan/id/${id}`,
    });

    this.ppInfo = ppState.remote({
      name: 'ppInfo',
      url: this.ppInfoUrl,
    });
  }

  componentDidMount() {
    const { id } = this.props;

    this.ppID.assign('PP', id);

    this.ppInfo.addListener('PP', () => {
      this.ppInfo.getState()
      .then(data => {
        this.setState({ data: data });
      });
    });
  }

  componentWillUnmount() {
    this.ppInfo.removeListener('PP');
  }

  getFeedbackSubject() {
    const { data } = this.state;

    let metadata;
    if (data !== undefined) {
      metadata = " - " + data.department.label + " - " + data.fiscalYear.name;
    }
    return escape(this.t("procurementPlan:feedbackSubject") + metadata);
  }

  render() {
    const { navigate, isFeatureVisible } = this.props;
    const { data } = this.state;

    const { currencyFormatter, formatDate } = this.props.styling.tables;

    return (<div className="procurement-plan makueni-form">
      <div className="row">
        <a href="#!/procurement-plan" onClick={() => navigate()} className="back-link col-md-3">
        <span className="back-icon">
          <span className="previous">&#8249;</span>
        </span>
          <span className="back-text">
            {this.t("general:goBack")}
        </span>
        </a>
      </div>

      <div className="row padding-top-10">
        <div className="col-md-12">
          <h1 className="page-title">{this.t("procurementPlan:title")}</h1>
        </div>
      </div>

      {
        data !== undefined
          ? <div>
            <div className="row">
              {isFeatureVisible("publicView.procurementPlan.department")
              && <Item label={this.t("procurementPlan:department")} value={data.department.label} col={8} />}
              {isFeatureVisible("publicView.procurementPlan.fiscalYear")
              && <Item label={this.t("procurementPlan:fiscalYear")} value={data.fiscalYear.name} col={4} />}
            </div>

            {
              data.planItems !== undefined && isFeatureVisible("publicView.procurementPlan.planItems")
                ? <div>
                  <div className="row padding-top-10">
                    <div className="col-md-12 sub-title">
                      {this.t("procurementPlan:item:caption").replace("$#$", data.planItems.length)}
                    </div>
                  </div>

                  {
                    data.planItems.map(planItem => <div key={planItem.id} className="box">
                      {isFeatureVisible("publicView.procurementPlan.planItems.label")
                      && <div className="row">
                        <Item label={this.t("procurementPlan:item:item")} value={planItem.item.label} col={12} />
                      </div>}

                      <div className="row display-flex">
                        {isFeatureVisible("publicView.procurementPlan.planItems.unitOfIssue")
                        && <Item label={this.t("procurementPlan:item:unitOfIssue")}
                                 value={planItem.unitOfIssue.label} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.quantity")
                        && <Item label={this.t("procurementPlan:item:quantity")}
                                 value={currencyFormatter(planItem.quantity)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.estimatedCost")
                        && <Item label={this.t("procurementPlan:item:estimatedCost")}
                                 value={currencyFormatter(planItem.estimatedCost)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.totalCost")
                        && <Item label={this.t("procurementPlan:item:totalCost")}
                                 value={currencyFormatter(planItem.quantity * planItem.estimatedCost)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.procurementMethod")
                        && <Item label={this.t("procurementPlan:item:procurementMethod")}
                                 value={planItem.procurementMethod.label} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.sourceOfFunds")
                        && <Item label={this.t("procurementPlan:item:sourceOfFunds")}
                                 value={planItem.sourceOfFunds} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.targetGroup")
                        && <Item label={this.t("procurementPlan:item:targetGroup")}
                                 value={(planItem.targetGroup || {}).label} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.targetGroupValue")
                        && <Item label={this.t("procurementPlan:item:targetGroupValue")}
                                 value={currencyFormatter(planItem.targetGroupValue)} col={3} />}
                      </div>

                      <div className="row">
                        <h4 className="col-md-12">{this.t("procurementPlan:item:timingOfActivities")}</h4>
                        {isFeatureVisible("publicView.procurementPlan.planItems.quarter1st")
                        && <Item label={this.t("procurementPlan:item:quarter1st")}
                                 value={currencyFormatter(planItem.quarter1st)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.quarter2nd")
                        && <Item label={this.t("procurementPlan:item:quarter2nd")}
                                 value={currencyFormatter(planItem.quarter2nd)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.quarter3rd")
                        && <Item label={this.t("procurementPlan:item:quarter3rd")}
                                 value={currencyFormatter(planItem.quarter3rd)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.quarter4th")
                        && <Item label={this.t("procurementPlan:item:quarter4th")}
                                 value={currencyFormatter(planItem.quarter4th)} col={3} />}
                      </div>
                    </div>)
                  }

                  <div className="row">
                    {isFeatureVisible("publicView.procurementPlan.formDocs")
                    && <Item label={this.t("procurementPlan:item:ppDocs")} col={6}>
                      <FileDownloadLinks files={data.formDocs || []} useDash />
                    </Item>}
                    {isFeatureVisible("publicView.procurementPlan.approvedDate")
                    && <Item label={this.t("procurementPlan:item:approvedDate")} value={formatDate(data.approvedDate)} col={6} />}
                  </div>
                </div>
                : null
            }
          </div>
          : null
      }

      {this.getFeedbackMessage()}
    </div>);
  }
}

export default fmConnect(ProcurementPlan);
