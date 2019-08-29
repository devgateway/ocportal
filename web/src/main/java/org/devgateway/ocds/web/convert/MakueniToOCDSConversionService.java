package org.devgateway.ocds.web.convert;


import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;

public interface MakueniToOCDSConversionService {

    Release createRelease(PurchaseRequisition purchaseRequisition);


    Release createAndPersistRelease(PurchaseRequisition purchaseRequisition);

    void convertToOcdsAndSaveAllApprovedPurchaseRequisitions();

}
