import React from 'react';
import TopImplSupplier from './top-impl-supplier';
import fmConnect from "../../fm/fm";

class TopSuppliersDelayedContracts extends TopImplSupplier {
}

TopSuppliersDelayedContracts.getName = t => t('tables:topSuppliersDelayedContracts:title');
TopSuppliersDelayedContracts.endpoint = 'topSuppliersDelayedContracts';
TopSuppliersDelayedContracts.contractListEndpoint= 'delayedContractNames';

export default fmConnect(TopSuppliersDelayedContracts, 'viz.me.table.topSuppliersDelayedContracts');
