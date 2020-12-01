import React from 'react';
import TopImplSupplier from './top-impl-supplier';

class TopSuppliersPmcReportNopay extends TopImplSupplier {
}

TopSuppliersPmcReportNopay.getName = t => t('tables:topSuppliersPmcNotAuthContracts:title');
TopSuppliersPmcReportNopay.endpoint = 'topSuppliersPmcNotAuthContracts';
TopSuppliersPmcReportNopay.contractListEndpoint= 'pmcNotAuthContractNames';

export default TopSuppliersPmcReportNopay;
