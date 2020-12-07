import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
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
    const { data, prId } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage translations={this.props.translations}/>);
    }

    const tender = data[0];

    return (<div>
      <div className="row padding-top-10">
        <div className="col-md-4">
          <div className="item-label">{this.t("tender:title")}</div>
          <div className="item-value">{tender.tenderTitle}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">{this.t("tender:number")}</div>
          <div className="item-value">{tender.tenderNumber}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">{this.t("tender:code")}</div>
          <div className="item-value">{prId}</div>
        </div>
      </div>

      <div className="row padding-top-10">

        <div className="col-md-4">
          <div className="item-label">{this.t("tender:procurementMethod")}</div>
          <div className="item-value">{tender.procurementMethod.label}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">{this.t("tender:procurementMethodRationale")}</div>
          <div className="item-value">
            {tender.procurementMethodRationale && tender.procurementMethodRationale.label}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">{this.t("tender:invitationDate")}</div>
          <div className="item-value">{formatDate(tender.invitationDate)}</div>
        </div>
      </div>
      <div className="row padding-top-10">
        <div className="col-md-4">
          <div className="item-label">{this.t("tender:closingDate")}</div>
          <div className="item-value">{formatDate(tender.closingDate)}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">{this.t("tender:issuedBy:label")}</div>
          <div className="item-value">{tender.issuedBy.label}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">{this.t("tender:issuedBy:address")}</div>
          <div className="item-value">{tender.issuedBy.address}</div>
        </div>
      </div>

      <div className="row padding-top-10">
        <div className="col-md-4">
          <div className="item-label">{this.t("tender:issuedBy:email")}</div>
          <div className="item-value download">
            <a href={"mailto:"+tender.issuedBy.emailAddress}>{tender.issuedBy.emailAddress}</a></div>
        </div>
        <div className="col-md-4">
          <div className="item-label">{this.t("tender:value")}</div>
          <div className="item-value">{currencyFormatter(tender.tenderValue)}</div>
        </div>
        <div className="col-md-4">
          <div className="item-label">{this.t("tender:targetGroup")}</div>
          {
            tender.targetGroup !== undefined
              ? <div className="item-value">{tender.targetGroup.label}</div>
              : null
          }
        </div>
      </div>
      
      <div className="row padding-top-10">
        <div className="col-md-12">
          <div className="item-label">{this.t("tender:objective")}</div>
          <div className="item-value">{tender.objective}</div>
        </div>
      </div>

      {
        tender.tenderItems !== undefined
          ? <div>
            <div className="row padding-top-10">
              <div className="col-md-12 sub-title">{this.t("tender:tenderItems")}
                ({tender.tenderItems.length})
              </div>
            </div>

            {
              tender.tenderItems.map(tenderItem => <div key={tenderItem._id} className="box">
                <div className="row">
                  <div className="col-md-6">
                    <div className="item-label">{this.t("tender:item")}</div>
                    <div
                      className="item-value">{tenderItem.purchaseItem.planItem.item.label}</div>
                  </div>
                  <div className="col-md-6">
                    <div className="item-label">{this.t("tender:item:description")}</div>
                    <div className="item-value">{tenderItem.description}</div>
                  </div>
                </div>
                <div className="row">
                  <div className="col-md-3">
                    <div className="item-label">{this.t("tender:item:unitOfIssue")}</div>
                    <div
                      className="item-value">{tenderItem.purchaseItem.planItem.unitOfIssue.label}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">{this.t("tender:item:quantity")}</div>
                    <div className="item-value">{currencyFormatter(tenderItem.quantity)}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">{this.t("tender:item:unitPrice")}</div>
                    <div className="item-value">{currencyFormatter(tenderItem.unitPrice)}</div>
                  </div>
                  <div className="col-md-3">
                    <div className="item-label">{this.t("tender:item:totalCost")}</div>
                    <div
                      className="item-value">{currencyFormatter(tenderItem.quantity * tenderItem.unitPrice)}</div>
                  </div>
                </div>
              </div>)
            }
          </div>
          : null
      }

      <div className="row padding-top-10">
        <div className="col-md-6"
             data-intro={this.t("tender:item:dlTender:dataIntro")}>
          <div className="item-label">{this.t("tender:item:dlTender")}</div>
          {
            tender.formDocs && tender.formDocs.map(doc => <div key={doc._id}>
              <OverlayTrigger
                placement="bottom"
                overlay={
                  <Tooltip id="download-tooltip">
                    {this.t("general:downloadFile:tooltip")}
                  </Tooltip>
                }>

                <a className="item-value download" href={doc.url} target="_blank">
                  <i className="glyphicon glyphicon-download"/>
                  <span>{doc.name}</span>
                </a>
              </OverlayTrigger>
            </div>)
          }
        </div>
        <div className="col-md-6">
          <div className="item-label">{this.t("tender:item:tenderLink")}</div>
          <div className="item-value">
            <a className="item-value download" href={tender.tenderLink} target="_blank">
              {tender.tenderLink}
            </a>

          </div>
        </div>
      </div>
    </div>);
  }
}

export default Tender;
