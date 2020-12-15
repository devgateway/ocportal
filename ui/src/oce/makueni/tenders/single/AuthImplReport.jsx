import React from 'react';
import {Item} from "./Item";
import ImplReport from "./ImplReport";
import PropTypes from "prop-types";
import {tCreator} from "../../../translatable";

const AuthImplReport = (props) => {

  const {authChildren, fmPrefix, childElements} = props;
  const t = tCreator(props.translations);

  const authChildElements = (i) => {
    const {formatBoolean} = props.styling.tables;
    const {isFeatureVisible} = props;
    return ([childElements && childElements(i), <div key="1" className="row">
      {isFeatureVisible(fmPrefix + ".authorizePayment")
      && <Item label={t("authImplReport:authorizePayment")} value={formatBoolean(i.authorizePayment)} col={3}/>}
      {
        authChildren && authChildren(i)
      }
    </div>]);
  }

  return (<ImplReport {...props} childElements={authChildElements}/>);
}

AuthImplReport.propTypes = {
  childElements: PropTypes.func,
  styling: PropTypes.object.isRequired,
  fiscalYear: PropTypes.object.isRequired,
  department: PropTypes.object.isRequired,
  reportName: PropTypes.string.isRequired,
  fmPrefix: PropTypes.string.isRequired,
  translations: PropTypes.object.isRequired,
  data: PropTypes.array,
  isFeatureVisible: PropTypes.func.isRequired,
  tenderTitle: PropTypes.string.isRequired,
  authChildren: PropTypes.func,
};
export default AuthImplReport;
