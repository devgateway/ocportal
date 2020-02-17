import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import NoDataMessage from './NoData';
import React from 'react';
import AuthImplReport from './AuthImplReport';

class AdministratorReport extends AuthImplReport {

getReportName() {
   return "Administrator Report";
}

}

export default AdministratorReport;
