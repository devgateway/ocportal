import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import translatable from "../../../translatable";

class Tender extends translatable(React.Component) {
  getFeedbackSubject() {
    const { data, department, fiscalYear } = this.props;

    let metadata;
    if (data !== undefined) {
      const tender = data[0];

      metadata = ' - ' + tender.tenderTitle
        + ' - ' + department.label
        + ' - ' + fiscalYear.name;
    }
    return escape(this.t("tender:label") + metadata);
  }

  render() {
    const { data, prId, isFeatureVisible } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    const tender = data[0];

    return (<div>
      <div className="row display-flex">
        {isFeatureVisible("publicView.tender.tenderTitle")
        && <Item label={this.t("tender:title")} value={tender.tenderTitle} col={4} />}

        {isFeatureVisible("publicView.tender.tenderNumber")
        && <Item label={this.t("tender:number")} value={tender.tenderNumber} col={4} />}

        {isFeatureVisible("publicView.tender.tenderCode")
        && <Item label={this.t("tender:code")} value={prId} col={4} />}

        {isFeatureVisible("publicView.tender.procurementMethod")
        && <Item label={this.t("tender:procurementMethod")} value={tender.procurementMethod.label} col={4} />}

        {isFeatureVisible("publicView.tender.procurementMethodRationale")
        && <Item label={this.t("tender:procurementMethodRationale")}
                 value={(tender.procurementMethodRationale || {}).label} col={4} />}

        {isFeatureVisible("publicView.tender.invitationDate")
        && <Item label={this.t("tender:invitationDate")} value={formatDate(tender.invitationDate)} col={4} />}

        {isFeatureVisible("publicView.tender.closingDate")
        && <Item label={this.t("tender:closingDate")} value={formatDate(tender.closingDate)} col={4} />}

        {isFeatureVisible("publicView.tender.issuedBy.label")
        && <Item label={this.t("tender:issuedBy:label")} value={tender.issuedBy.label} col={4} />}

        {isFeatureVisible("publicView.tender.issuedBy.address")
        && <Item label={this.t("tender:issuedBy:address")} value={tender.issuedBy.address} col={4} />}

        {isFeatureVisible("publicView.tender.issuedBy.emailAddress")
        && <Item label={this.t("tender:issuedBy:email")} col={4}>
          {tender.issuedBy.emailAddress
            ? <a className="download" href={"mailto:"+tender.issuedBy.emailAddress}>
              {tender.issuedBy.emailAddress}
            </a>
            : "-"
          }
        </Item>}

        {isFeatureVisible("publicView.tender.tenderValue")
        && <Item label={this.t("tender:value")} value={currencyFormatter(tender.tenderValue)} col={4} />}

        {isFeatureVisible("publicView.tender.targetGroup")
        && <Item label={this.t("tender:targetGroup")} value={tender.targetGroup && tender.targetGroup.label} col={4} />}

        {isFeatureVisible("publicView.tender.objective")
        && <Item label={this.t("tender:objective")} value={tender.objective} col={12} />}
      </div>

      {
        tender.tenderItems !== undefined && isFeatureVisible("publicView.tender.tenderItems")
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">{this.t("tender:tenderItems")}
                ({tender.tenderItems.length})
              </div>
            </div>

            {
              tender.tenderItems.map(tenderItem => <div key={tenderItem._id} className="box">
                <div className="row display-flex">
                  {isFeatureVisible("publicView.tender.tenderItems.item")
                  && <Item label={this.t("tender:item")} value={tenderItem.purchaseItem.planItem.item.label} col={6} />}

                  {isFeatureVisible("publicView.tender.tenderItems.description")
                  && <Item label={this.t("tender:item:description")} value={tenderItem.description} col={6} />}

                  {isFeatureVisible("publicView.tender.tenderItems.purchaseItem")
                  && <Item label={this.t("tender:item:unitOfIssue")} value={tenderItem.purchaseItem.planItem.unitOfIssue.label}
                           col={3} />}

                  {isFeatureVisible("publicView.tender.tenderItems.quantity")
                  && <Item label={this.t("tender:item:quantity")} value={currencyFormatter(tenderItem.quantity)} col={3} />}

                  {isFeatureVisible("publicView.tender.tenderItems.unitPrice")
                  && <Item label={this.t("tender:item:unitPrice")} value={currencyFormatter(tenderItem.unitPrice)} col={3} />}

                  {isFeatureVisible("publicView.tender.tenderItems.totalCost")
                  && <Item label={this.t("tender:item:totalCost")}
                           value={currencyFormatter(tenderItem.quantity * tenderItem.unitPrice)} col={3} />}
                </div>
              </div>)
            }
          </div>
          : null
      }

      <div className="row">
        {isFeatureVisible("publicView.tender.formDocs")
        && <Item label={this.t("tender:item:dlTender")} col={6}
                 data-intro={this.t("tender:item:dlTender:dataIntro")}>
          <FileDownloadLinks files={tender.formDocs || []} useDash />
        </Item>}

        {isFeatureVisible("publicView.tender.tenderLink")
        && <Item label={this.t("tender:item:tenderLink")} col={6}>
          {tender.tenderLink
            ? <a className="download" href={tender.tenderLink} target="_blank">
              {tender.tenderLink}
            </a>
            : "-"
          }
        </Item>}
      </div>
    </div>);
  }
}

export default fmConnect(Tender);
