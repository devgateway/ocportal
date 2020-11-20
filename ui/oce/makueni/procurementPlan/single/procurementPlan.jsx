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
    return escape("Procurement Plan" + metadata);
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
          Go Back
        </span>
        </a>
      </div>

      <div className="row padding-top-10">
        <div className="col-md-12">
          <h1 className="page-title">Procurement Plan</h1>
        </div>
      </div>

      {
        data !== undefined
          ? <div>
            <div className="row">
              {isFeatureVisible("publicView.procurementPlan.department")
              && <Item label="Department" value={data.department.label} col={8} />}
              {isFeatureVisible("publicView.procurementPlan.fiscalYear")
              && <Item label="Fiscal Year" value={data.fiscalYear.name} col={4} />}
            </div>

            {
              data.planItems !== undefined && isFeatureVisible("publicView.procurementPlan.planItems")
                ? <div>
                  <div className="row padding-top-10">
                    <div className="col-md-12 sub-title">Procurement Plan Item
                      ({data.planItems.length})
                    </div>
                  </div>

                  {
                    data.planItems.map(planItem => <div key={planItem.id} className="box">
                      {isFeatureVisible("publicView.procurementPlan.planItems.label")
                      && <div className="row">
                        <Item label="Item" value={planItem.item.label} col={12} />
                      </div>}

                      <div className="row display-flex">
                        {isFeatureVisible("publicView.procurementPlan.planItems.unitOfIssue")
                        && <Item label="Unit Of Issue" value={planItem.unitOfIssue.label} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.quantity")
                        && <Item label="Quantity" value={currencyFormatter(planItem.quantity)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.estimatedCost")
                        && <Item label="Estimated Cost per Unit" value={currencyFormatter(planItem.estimatedCost)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.totalCost")
                        && <Item label="Total Cost" value={currencyFormatter(planItem.quantity * planItem.estimatedCost)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.procurementMethod")
                        && <Item label="Procurement Method" value={planItem.procurementMethod.label} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.sourceOfFunds")
                        && <Item label="Account" value={planItem.sourceOfFunds} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.targetGroup")
                        && <Item label="Target Group" value={(planItem.targetGroup || {}).label} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.targetGroupValue")
                        && <Item label="Target Group Value" value={currencyFormatter(planItem.targetGroupValue)} col={3} />}
                      </div>

                      <div className="row">
                        <h4 className="col-md-12">Timing of activities (quarterly basis)</h4>
                        {isFeatureVisible("publicView.procurementPlan.planItems.quarter1st")
                        && <Item label="1st Quarter" value={currencyFormatter(planItem.quarter1st)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.quarter2nd")
                        && <Item label="2nd Quarter" value={currencyFormatter(planItem.quarter2nd)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.quarter3rd")
                        && <Item label="3rd Quarter" value={currencyFormatter(planItem.quarter3rd)} col={3} />}
                        {isFeatureVisible("publicView.procurementPlan.planItems.quarter4th")
                        && <Item label="4th Quarter" value={currencyFormatter(planItem.quarter4th)} col={3} />}
                      </div>
                    </div>)
                  }

                  <div className="row">
                    {isFeatureVisible("publicView.procurementPlan.formDocs")
                    && <Item label="Procurement Plan Documents" col={6}>
                      <FileDownloadLinks files={data.formDocs || []} />
                    </Item>}
                    {isFeatureVisible("publicView.procurementPlan.approvedDate")
                    && <Item label="Approved Date" value={formatDate(data.approvedDate)} col={6} />}
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
