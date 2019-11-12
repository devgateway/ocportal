package org.devgateway.ocds.web.convert;


import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;

public interface MakueniToOCDSConversionService {

    Release createRelease(TenderProcess tenderProcess);


    Release createAndPersistRelease(TenderProcess tenderProcess);

    void convertToOcdsAndSaveAllApprovedPurchaseRequisitions();

}
