import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";
import FileDownloadLinks from "./FileDownloadLinks";
import {tCreator} from "../../../translatable";
import NoDataMessage from "./NoData";
import defaultSingleTenderTabTypes from "./singleUtil";

const ProfessionalOpinion = (props) => {
  const t = tCreator(props.translations);
  const {data, isFeatureVisible} = props;
  const {currencyFormatter, formatDate} = props.styling.tables;

  const getFeedbackSubject = () => {
    const {tenderTitle, department, fiscalYear} = props;


    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
          + " - " + department.label
          + " - " + fiscalYear.name;
    }
    return escape(t("professionalOpinion:label") + metadata);
  }


  const getProfessionalOpinion = (professionalOpinion) => {
    return (<div>
      <div className="padding-top-10">
        {
          professionalOpinion.items !== undefined && isFeatureVisible("publicView.professionalOpinions.items")
              ? professionalOpinion.items.map(i => <div key={i._id} className="box">
                <div className="row">
                  {isFeatureVisible("publicView.professionalOpinions.items.professionalOpinionDate")
                  && <Item label={t("professionalOpinion:professionalOpinionDate")}
                           value={formatDate(i.professionalOpinionDate)} col={4}/>}
                  {isFeatureVisible("publicView.professionalOpinions.items.awardee")
                  && <Item label={t("professionalOpinion:awardee")} value={i.awardee.label} col={4}/>}
                  {isFeatureVisible("publicView.professionalOpinions.items.recommendedAwardAmount")
                  && <Item label={t("professionalOpinion:recommendedAwardAmount")}
                           value={currencyFormatter(i.recommendedAwardAmount)} col={4}/>}
                  {isFeatureVisible("publicView.professionalOpinions.items.formDocs")
                  && <Item label={t("professionalOpinion:docs")} col={4}>
                    <FileDownloadLinks files={i.formDocs} useDash/>
                  </Item>}
                  {isFeatureVisible("publicView.professionalOpinions.items.approvedDate")
                  && <Item label={t("professionalOpinion:approvedDate")}
                           value={formatDate(i.approvedDate)} col={4}/>}
                </div>
              </div>
              ) : null
        }
      </div>
    </div>);
  }

  return (data === undefined ? <NoDataMessage translations={props.translations}/> : getProfessionalOpinion(data[0]));
}

ProfessionalOpinion.propTypes = defaultSingleTenderTabTypes;

export default fmConnect(ProfessionalOpinion);
