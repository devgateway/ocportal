import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import FeedbackPage from '../../FeedbackPage';
import NoDataMessage from './NoData';

class Notification extends FeedbackPage {
  getFeedbackSubject() {
    const { tenderTitle, department, fiscalYear } = this.props;

    let metadata;
    if (department !== undefined) {
      metadata = " - " + tenderTitle
        + " - " + department.label
        + " - " + fiscalYear.name;
    }
    return escape("Notification" + metadata);
  }

  render() {
    const { data } = this.props;
    const { currencyFormatter, formatDate } = this.props.styling.tables;

    if (data === undefined) {
      return (<NoDataMessage/>);
    }

    const awardNotification = data[0];

    return (<div>
      <div className="row padding-top-10">
        {
          awardNotification.items !== undefined
            ? awardNotification.items.map(i => <div key={i._id} className="box">
              <div className="row padding-top-10">
                <div className="col-md-4">
                  <div className="item-label">Award Value</div>
                  <div className="item-value">{currencyFormatter(i.awardValue)}</div>
                </div>
                <div className="col-md-4">
                  <div className="item-label">Date</div>
                  <div className="item-value">{formatDate(i.awardDate)}</div>
                </div>
                <div className="col-md-4">
                  <div className="item-label">Acknowledge Receipt of Award Timeline</div>
                  <div className="item-value">{i.acknowledgementDays}</div>
                </div>
              </div>

              <div className="row padding-top-10">
                <div className="col-md-4">
                  <div className="item-label">Supplier Name</div>
                  <div className="item-value">{i.awardee.label}</div>
                </div>
                <div className="col-md-4">
                  <div className="item-label">Winning Bid Supplier ID</div>
                  <div className="item-value">{i.awardee.code}</div>
                </div>
                <div className="col-md-4">
                  <div className="item-label">Supplier Postal Address</div>
                  <div className="item-value">{i.awardee.address}</div>
                </div>
              </div>

              <div className="row padding-top-10">
                <div className="col-md-12">
                  <div className="item-label">Letter of Notification of Award</div>

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

            </div>
            ) : null
        }
      </div>
      {this.getFeedbackMessage()}
    </div>);
  }
}

export default Notification;
