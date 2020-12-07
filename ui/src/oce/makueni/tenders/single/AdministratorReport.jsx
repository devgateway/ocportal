import React from 'react';
import AuthImplReport from './AuthImplReport';

class AdministratorReport extends AuthImplReport {

getReportName() {
   return this.t("administratorReport:reportName");
}

}

export default AdministratorReport;
