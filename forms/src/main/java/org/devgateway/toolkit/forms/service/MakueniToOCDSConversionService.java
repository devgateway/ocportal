package org.devgateway.toolkit.forms.service;


import org.devgateway.ocds.persistence.mongo.Amount;
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.Bids;
import org.devgateway.ocds.persistence.mongo.Budget;
import org.devgateway.ocds.persistence.mongo.Classification;
import org.devgateway.ocds.persistence.mongo.Contract;
import org.devgateway.ocds.persistence.mongo.Item;
import org.devgateway.ocds.persistence.mongo.MakueniPlanning;
import org.devgateway.ocds.persistence.mongo.Milestone;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Planning;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.Unit;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;

import java.math.BigDecimal;
import java.util.Set;

public interface MakueniToOCDSConversionService {

    Tender createTender(org.devgateway.toolkit.persistence.dao.form.Tender tender);

    Budget createPlanningBudget(PurchaseRequisition purchaseRequisition);

    MakueniPlanning createPlanning(PurchaseRequisition purchaseRequisition);

    Unit createPlanningItemUnit(PurchaseItem item);

    Amount convertAmount(BigDecimal amount);

    Item createPlanningItem(PurchaseItem item);

    Classification createPlanningItemClassification(PurchaseItem item);

    Amount.Currency getCurrency();

    Set<Organization> createParties(Tender tender, Planning planning);

    Bids createBids(TenderQuotationEvaluation quotationEvaluation);

    Award createAward(ProfessionalOpinion professionalOpinion, AwardNotification awardNotification,
                      AwardAcceptance awardAcceptance);

    Contract createContract(org.devgateway.toolkit.persistence.dao.form.Contract contract);

    Release createRelease(PurchaseRequisition purchaseRequisition);

    Milestone createPlanningMilestone(PurchaseRequisition purchaseRequisition);
}
