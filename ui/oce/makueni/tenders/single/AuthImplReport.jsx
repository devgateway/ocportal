import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
import ImplReport from './ImplReport';

class AuthImplReport extends ImplReport {

  childElements(i) {
    const { formatDate, formatBoolean } = this.props.styling.tables;
    return (<div className="row padding-top-10">
      <div className="col-md-4">
        <div className="item-label">Authorize Payment</div>
        <div className="item-value">{formatBoolean(i.authorizePayment)}</div>
      </div>
      <div className="col-md-4">
        <div className="item-label">Report Date</div>
        <div className="item-value">{formatDate(i.approvedDate)}</div>
      </div>
    </div>);
  }
}

export default AuthImplReport;
