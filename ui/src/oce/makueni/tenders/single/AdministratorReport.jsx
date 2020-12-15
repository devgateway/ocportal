import React from 'react';
import AuthImplReport from "./AuthImplReport";
import {tCreator} from "../../../translatable";
import fmConnect from "../../../fm/fm";

const AdministratorReport = (props) => {

   const t = tCreator(props.translations);

   return (<AuthImplReport {...props} reportName={t("administratorReport:reportName")}
                           fmPrefix={"publicView.administratorReport"}/>);
}

export default fmConnect(AdministratorReport);
