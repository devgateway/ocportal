import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import {tCreator} from "../../../translatable";
import defaultSingleTenderTabTypes from "./singleUtil";

const Award = (props) => {
  const {tenderTitle, department, fiscalYear} = props;
  const t = tCreator(props.translations);
  const {data, isFeatureVisible} = props;
  const {currencyFormatter, formatDate} = props.styling.tables;
  
  const getFeedbackSubject = () => {
    let metadata;
    if (department !== undefined) {
      metadata = ' - ' + tenderTitle
          + ' - ' + department.label
          + ' - ' + fiscalYear.name;
    }
    return escape(t("award:subject") + metadata);
  }

  const getAward = (awardAcceptance) => {
    return (<div>
      <div className="padding-top-10">
        {
          awardAcceptance.items !== undefined && isFeatureVisible("publicView.awardAcceptance.items")
              ? awardAcceptance.items.map(i => <div key={i._id} className="box">

                <div className="row">
                  {isFeatureVisible("publicView.awardAcceptance.items.supplierResponse")
                  && <Item label={t("award:supplierResponse")} value={i.supplierResponse.label} col={3}/>}

                  {isFeatureVisible("publicView.awardAcceptance.items.acceptedAwardValue")
                  &&
                  <Item label={t("award:acceptedAwardValue")} value={currencyFormatter(i.acceptedAwardValue)}
                        col={3}/>}

                  {isFeatureVisible("publicView.awardAcceptance.items.acceptanceDate")
                  && <Item label={t("award:acceptanceDate")} value={formatDate(i.acceptanceDate)} col={3}/>}

                  {isFeatureVisible("publicView.awardAcceptance.items.awardee.label")
                  && <Item label={t("award:awardeeLabel")} value={i.awardee.label} col={3}/>}

                  {isFeatureVisible("publicView.awardAcceptance.items.awardee.code")
                  && <Item label={t("award:awardeeCode")} value={i.awardee.code} col={3}/>}

                  {isFeatureVisible("publicView.awardAcceptance.items.formDocs")
                  && <Item label={t("award:letterOfAcceptanceOfAward")} col={12}>
                    <FileDownloadLinks files={i.formDocs} useDash/>
                  </Item>}
                </div>
              </div>) : null
        }
      </div>
    </div>);
  }

  return (data === undefined ? <NoDataMessage translations={props.translations}/> : getAward(data[0]));
}

Award.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(Award);
