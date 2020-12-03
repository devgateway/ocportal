import React from 'react';
import AuthImplReport from './AuthImplReport';
import fmConnect from "../../../fm/fm";

class AdministratorReport extends AuthImplReport {

   getReportName() {
      return this.t("administratorReport:reportName");
   }

   getFMPrefix() {
      return "publicView.administratorReport"
   }
}

export default fmConnect(AdministratorReport);
