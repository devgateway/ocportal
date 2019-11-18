import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import FeedbackPage from '../../FeedbackPage';

class Award extends FeedbackPage {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = ' - ' + tenderTitle
        + ' - ' + department.label
        + ' - ' + fiscalYear.name;
    }
    return escape('Award' + metadata);
  }

  render() {
    const { data } = this.props;

    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return null;
    }

    const awardAcceptance = data[0];

    return (<div>
      <div className="row padding-top-10">
        {
          awardAcceptance.items !== undefined
            ? awardAcceptance.items.map(i => <div key={i._id} className="box">
              <div className="row padding-top-10">
                <div className="col-md-3">
                  <div className="item-label">Supplier Response</div>
                <div
                  className="item-value">{i.supplierResponse.label}</div>
                </div>
              </div>
              <div className="row padding-top-10">
                <div className="col-md-3">
                  <div className="item-label">Accepted Award Value</div>
                  <div
                    className="item-value">{currencyFormatter(i.acceptedAwardValue)}</div>
                </div>
                <div className="col-md-3">
                  <div className="item-label">Date</div>
                  <div className="item-value">{formatDate(i.acceptanceDate)}</div>
                </div>
                <div className="col-md-3">
                  <div className="item-label">Supplier Name</div>
                  <div className="item-value">{i.awardee.label}</div>
                </div>
                <div className="col-md-3">
                  <div className="item-label">Supplier ID</div>
                  <div className="item-value">{i.awardee.code}</div>
                </div>
              </div>

              <div className="row padding-top-10">
                <div className="col-md-12">
                  <div className="item-label">Letter of Acceptance of Award</div>

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
              </div>
            </div>) : null
        }
      </div>
      {this.getFeedbackMessage()}
    </div>);
  }
}

export default Award;
