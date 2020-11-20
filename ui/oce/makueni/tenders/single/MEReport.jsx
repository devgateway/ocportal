import React from 'react';
import ImplReport from './ImplReport';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";

class MEReport extends ImplReport {

  getReportName() {
    return 'M&E Reports';
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
        && <Item label="SNO" value={i.sno} col={3} />}

        {isFeatureVisible("publicView.meReport.subcounties")
        && <Item label="Sub-Counties" value={i.subcounties.map(item => item.label).join(', ')} col={3} />}

        {isFeatureVisible("publicView.meReport.wards")
        && <Item label="Wards" value={i.wards.map(item => item.label).join(', ')} col={3} />}

        {isFeatureVisible("publicView.meReport.subwards")
        && <Item label="Sub-Wards" value={i.subwards && i.subwards.map(item => item.label).join(', ')} col={3} />}

        {isFeatureVisible("publicView.meReport.lpoAmount")
        && <Item label="LPO Amount" value={currencyFormatter(i.lpoAmount)} col={3} />}

        {isFeatureVisible("publicView.meReport.lpoNumber")
        && <Item label="LPO Number" value={i.lpoNumber} col={3} />}

        {isFeatureVisible("publicView.meReport.expenditure")
        && <Item label="Expenditure" value={currencyFormatter(i.expenditure)} col={3} />}

        {isFeatureVisible("publicView.meReport.uncommitted")
        && <Item label="Uncommitted Funds / Unabsorbed Funds / Vote Balance"
                 value={currencyFormatter(i.uncommitted)} col={3} />}

        {isFeatureVisible("publicView.meReport.projectScope")
        && <Item label="Project Scope" value={i.projectScope} col={3} />}

        {isFeatureVisible("publicView.meReport.output")
        && <Item label="Output" value={i.output} col={3} />}

        {isFeatureVisible("publicView.meReport.outcome")
        && <Item label="Outcome" value={i.outcome} col={3} />}

        {isFeatureVisible("publicView.meReport.projectProgress")
        && <Item label="Project Progress" value={i.projectProgress} col={3} />}

        {isFeatureVisible("publicView.meReport.directBeneficiariesTarget")
        && <Item label="Target number of direct beneficiaries" value={i.directBeneficiariesTarget} col={3} />}

        {isFeatureVisible("publicView.meReport.wayForward")
        && <Item label="Way Forward" value={i.wayForward} col={3} />}

        {isFeatureVisible("publicView.meReport.byWhen")
        && <Item label="By When" value={formatDate(i.byWhen)} col={3} />}

        {isFeatureVisible("publicView.meReport.inspected")
        && <Item label="Inspected" value={formatBoolean(i.inspected)} col={3} />}

        {isFeatureVisible("publicView.meReport.invoiced")
        && <Item label="Invoiced" value={formatBoolean(i.invoiced)} col={3} />}

        {isFeatureVisible("publicView.meReport.officerResponsible")
        && <Item label="Officer Responsible" value={i.officerResponsible} col={3} />}

        {isFeatureVisible("publicView.meReport.meStatus")
        && <Item label="M&E Status" value={i.meStatus.label} col={3} />}

        {isFeatureVisible("publicView.meReport.remarks")
        && <Item label="Remarks" value={i.remarks} col={3} />}

        {isFeatureVisible("publicView.meReport.contractorContact")
        && <Item label="Contractor Contact" value={i.contractorContact} col={3} />}

        {isFeatureVisible("publicView.meReport.contractDate")
        && <Item label="Contract Date" value={formatDate(i.contract.contractDate)} col={3} />}

        {isFeatureVisible("publicView.meReport.contractExpiryDate")
        && <Item label="Contract Expirity Date" value={formatDate(i.contract.expiryDate)} col={3} />}

        {isFeatureVisible("publicView.meReport.amountBudgeted")
        && <Item label="Revised Budget" value={currencyFormatter(i.amountBudgeted)} col={3} />}
      </div>
    </div>);
  }

}

export default fmConnect(MEReport);
