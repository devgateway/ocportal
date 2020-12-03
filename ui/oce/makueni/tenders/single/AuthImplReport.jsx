import React from 'react';
import ImplReport from './ImplReport';
import {Item} from "./Item";

class AuthImplReport extends ImplReport {

  childElements(i) {
    const { formatBoolean } = this.props.styling.tables;
    const { isFeatureVisible } = this.props;
    return (<div key="1" className="row">
      {isFeatureVisible(this.getFMPrefix() + ".authorizePayment")
      && <Item label={this.t("authImplReport:authorizePayment")} value={formatBoolean(i.authorizePayment)} col={3} />}

      {
        this.authChildren(i)
      }
    </div>);
  }

  authChildren(i) {
  }
}

export default AuthImplReport;
