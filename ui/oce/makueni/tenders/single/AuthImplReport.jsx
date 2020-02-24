import React from 'react';
import ImplReport from './ImplReport';

class AuthImplReport extends ImplReport {

  childElements(i) {
    const { formatBoolean } = this.props.styling.tables;
    return (<div key="1" className="row padding-top-10">
      <div className="col-md-3">
        <div className="item-label">Authorize Payment</div>
        <div className="item-value">{formatBoolean(i.authorizePayment)}</div>
      </div>
      {
        this.authChildren(i)
      }
    </div>);
  }

  authChildren(i) {
  }
}

export default AuthImplReport;
