import React from 'react';
import TopImplSupplier from './top-impl-supplier';

class TopSuppliersDelayedContracts extends TopImplSupplier {
}

TopSuppliersDelayedContracts.getName = t => t('tables:topSuppliersDelayedContracts:title');
TopSuppliersDelayedContracts.endpoint = 'topSuppliersDelayedContracts';
TopSuppliersDelayedContracts.contractListEndpoint= 'delayedContractNames';

export default TopSuppliersDelayedContracts;
