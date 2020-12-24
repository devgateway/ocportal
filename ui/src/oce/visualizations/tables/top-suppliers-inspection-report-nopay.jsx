import React from 'react';
import TopImplSupplier from './top-impl-supplier';
import fmConnect from '../../fm/fm';

class TopSuppliersInspectionReportNoPay extends TopImplSupplier {
}

TopSuppliersInspectionReportNoPay.getName = (t) => t('tables:topSuppliersInspectionNoPay:title');
TopSuppliersInspectionReportNoPay.endpoint = 'topSuppliersInspectionNoPay';
TopSuppliersInspectionReportNoPay.contractListEndpoint = 'inspectionNoPayContractNames';

export default fmConnect(TopSuppliersInspectionReportNoPay, 'viz.me.table.topSuppliersInspectionReportNoPay');
