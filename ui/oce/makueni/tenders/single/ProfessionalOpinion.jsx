import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';

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
    const { data } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage/>);
    }

    const professionalOpinion = data[0];

    return (<div>
      <div className="row padding-top-10">
      {
        professionalOpinion.items !== undefined
          ? professionalOpinion.items.map(i => <div key={i._id} className="box">
            <div className="row padding-top-10">
              <div className="col-md-4">
                <div className="item-label">Professional Opinion Date</div>
                <div
                  className="item-value">{formatDate(i.professionalOpinionDate)}</div>
              </div>
              <div className="col-md-4">
                <div className="item-label">Awardee</div>
                <div className="item-value">{i.awardee.label}</div>
              </div>
              <div className="col-md-4">
                <div className="item-label">Recommended Award Amount</div>
                <div
                  className="item-value">{currencyFormatter(i.recommendedAwardAmount)}</div>
              </div>
            </div>

            <div className="row padding-top-10">
              <div className="col-md-6">
                <div className="item-label">Professional Opinion Documents</div>

                {
                  i.formDocs.map(doc => <div key={doc._id}>
                    <OverlayTrigger
                      placement="bottom"
                      overlay={
                        <Tooltip id="download-tooltip">
                          Click to download the file
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
                <div className="item-label">Approved Date</div>
                <div
                  className="item-value">{formatDate(i.approvedDate)}</div>
              </div>
            </div>
          </div>
          ) : null
      }
      </div>
    </div>);
  }
}

export default ProfessionalOpinion;
