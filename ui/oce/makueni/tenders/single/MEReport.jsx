import React from 'react';
import ImplReport from './ImplReport';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";

class MEReport extends ImplReport {

  getReportName() {
    return this.t("meReport:label");
  }

  getFMPrefix() {
    return "publicView.meReport"
  }

  childElements(i) {
    const { currencyFormatter, formatDate, formatBoolean } = this.props.styling.tables;
    const { isFeatureVisible } = this.props;
    return (<div>
      <div className="row display-flex">
        {isFeatureVisible("publicView.meReport.sno")
        && <Item label={this.t("meReport:sno")} value={i.sno} col={3} />}

        {isFeatureVisible("publicView.meReport.subcounties")
        && <Item label={this.t("meReport:subcounties")}
                 value={i.subcounties.map(item => item.label).join(', ')} col={3} />}

        {isFeatureVisible("publicView.meReport.wards")
        && <Item label={this.t("meReport:wards")} value={i.wards.map(item => item.label).join(', ')} col={3} />}

        {isFeatureVisible("publicView.meReport.subwards")
        && <Item label={this.t("meReport:subwards")}
                 value={i.subwards && i.subwards.map(item => item.label).join(', ')} col={3} />}

        {isFeatureVisible("publicView.meReport.lpoAmount")
        && <Item label={this.t("meReport:lpoAmount")} value={currencyFormatter(i.lpoAmount)} col={3} />}

        {isFeatureVisible("publicView.meReport.lpoNumber")
        && <Item label={this.t("meReport:lpoNumber")} value={i.lpoNumber} col={3} />}

        {isFeatureVisible("publicView.meReport.expenditure")
        && <Item label={this.t("meReport:expenditure")} value={currencyFormatter(i.expenditure)} col={3} />}

        {isFeatureVisible("publicView.meReport.uncommitted")
        && <Item label={this.t("meReport:uncommitted")} value={currencyFormatter(i.uncommitted)} col={3} />}

        {isFeatureVisible("publicView.meReport.projectScope")
        && <Item label={this.t("meReport:projectScope")} value={i.projectScope} col={3} />}

        {isFeatureVisible("publicView.meReport.output")
        && <Item label={this.t("meReport:output")} value={i.output} col={3} />}

        {isFeatureVisible("publicView.meReport.outcome")
        && <Item label={this.t("meReport:outcome")} value={i.outcome} col={3} />}

        {isFeatureVisible("publicView.meReport.projectProgress")
        && <Item label={this.t("meReport:projectProgress")} value={i.projectProgress} col={3} />}

        {isFeatureVisible("publicView.meReport.directBeneficiariesTarget")
        && <Item label={this.t("meReport:directBeneficiariesTarget")} value={i.directBeneficiariesTarget} col={3} />}

        {isFeatureVisible("publicView.meReport.wayForward")
        && <Item label={this.t("meReport:wayForward")} value={i.wayForward} col={3} />}

        {isFeatureVisible("publicView.meReport.byWhen")
        && <Item label={this.t("meReport:byWhen")} value={formatDate(i.byWhen)} col={3} />}

        {isFeatureVisible("publicView.meReport.inspected")
        && <Item label={this.t("meReport:inspected")} value={formatBoolean(i.inspected)} col={3} />}

        {isFeatureVisible("publicView.meReport.invoiced")
        && <Item label={this.t("meReport:invoiced")} value={formatBoolean(i.invoiced)} col={3} />}

        {isFeatureVisible("publicView.meReport.officerResponsible")
        && <Item label={this.t("meReport:officerResponsible")} value={i.officerResponsible} col={3} />}

        {isFeatureVisible("publicView.meReport.meStatus")
        && <Item label={this.t("meReport:meStatus")} value={i.meStatus.label} col={3} />}

        {isFeatureVisible("publicView.meReport.remarks")
        && <Item label={this.t("meReport:remarks")} value={i.remarks} col={3} />}

        {isFeatureVisible("publicView.meReport.contractorContact")
        && <Item label={this.t("meReport:contractorContact")} value={i.contractorContact} col={3} />}

        {isFeatureVisible("publicView.meReport.contractDate")
        && <Item label={this.t("meReport:contractDate")} value={formatDate(i.contract.contractDate)} col={3} />}

        {isFeatureVisible("publicView.meReport.contractExpiryDate")
        && <Item label={this.t("meReport:expiryDate")} value={formatDate(i.contract.expiryDate)} col={3} />}

        {isFeatureVisible("publicView.meReport.amountBudgeted")
        && <Item label={this.t("meReport:amountBudgeted")} value={currencyFormatter(i.amountBudgeted)} col={3} />}
      </div>
    </div>);
  }

}

export default fmConnect(MEReport);
