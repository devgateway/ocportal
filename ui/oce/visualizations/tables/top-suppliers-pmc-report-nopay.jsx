import React from 'react';
import TopImplSupplier from './top-impl-supplier';
import fmConnect from "../../fm/fm";

class TopSuppliersPmcReportNopay extends TopImplSupplier {
}

TopSuppliersPmcReportNopay.getName = t => t('tables:topSuppliersPmcNotAuthContracts:title');
TopSuppliersPmcReportNopay.endpoint = 'topSuppliersPmcNotAuthContracts';
TopSuppliersPmcReportNopay.contractListEndpoint= 'pmcNotAuthContractNames';

export default fmConnect(TopSuppliersPmcReportNopay, 'viz.me.table.topSuppliersPmcReportNoPay');
