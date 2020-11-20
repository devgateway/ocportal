import React from 'react';
import AuthImplReport from './AuthImplReport';
import fmConnect from "../../../fm/fm";

class AdministratorReport extends AuthImplReport {

   getReportName() {
      return "Administrator Reports";
   }

   getFMPrefix() {
      return "publicView.administratorReport"
   }
}

export default fmConnect(AdministratorReport);
