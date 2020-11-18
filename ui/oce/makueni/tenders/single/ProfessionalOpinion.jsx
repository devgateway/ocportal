import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
import fmConnect from "../../../fm/fm";
import {Item} from "./Item";

class ProfessionalOpinion extends React.Component {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
        + " - " + department.label
        + " - " + fiscalYear.name;
    }
    return escape("Professional Opinion" + metadata);
  }

  render() {
    const { data, isFeatureVisible } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage/>);
    }

    const professionalOpinion = data[0];

    return (<div>
      <div className="row padding-top-10">
      {
        professionalOpinion.items !== undefined && isFeatureVisible("professionalOpinionForm.items")
          ? professionalOpinion.items.map(i => <div key={i._id} className="box">
            <div className="row padding-top-10">
              {isFeatureVisible("professionalOpinionForm.items.professionalOpinionDate")
              && <Item label="Professional Opinion Date" value={formatDate(i.professionalOpinionDate)} col={4} />}
              {isFeatureVisible("professionalOpinionForm.items.awardee")
              && <Item label="Awardee" value={i.awardee.label} col={4} />}
              {isFeatureVisible("professionalOpinionForm.items.recommendedAwardAmount")
              && <Item label="Recommended Award Amount" value={currencyFormatter(i.recommendedAwardAmount)} col={4} />}
            </div>

            <div className="row padding-top-10">
              {isFeatureVisible("professionalOpinionForm.items.formDocs")
              && <Item label="Professional Opinion Documents" col={6}>
                {
                  i.formDocs.map(doc => <div key={doc._id}>
                    <OverlayTrigger
                      placement="bottom"
                      overlay={
                        <Tooltip id="download-tooltip">
                          Click to download the file
                        </Tooltip>
                      }>

                      <a className="download" href={doc.url} target="_blank">
                        <i className="glyphicon glyphicon-download"/>
                        <span>{doc.name}</span>
                      </a>
                    </OverlayTrigger>
                  </div>)
                }
              </Item>}
              {isFeatureVisible("professionalOpinionForm.items.approvedDate")
              && <Item label="Approved Date" value={formatDate(i.approvedDate)} col={6} />}
            </div>
          </div>
          ) : null
      }
      </div>
    </div>);
  }
}

export default fmConnect(ProfessionalOpinion);
