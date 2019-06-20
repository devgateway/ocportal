package org.devgateway.toolkit.forms.service;


import org.devgateway.ocds.persistence.mongo.Amount;
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.Bids;
import org.devgateway.ocds.persistence.mongo.Budget;
import org.devgateway.ocds.persistence.mongo.Classification;
import org.devgateway.ocds.persistence.mongo.Contract;
import org.devgateway.ocds.persistence.mongo.Item;
import org.devgateway.ocds.persistence.mongo.MakueniPlanning;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Planning;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.Unit;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;

import java.util.Set;

public interface MakueniToOCDSConversionService {

    Tender createTender(org.devgateway.toolkit.persistence.dao.form.Tender tender);

    Budget createPlanningBudget(ProcurementPlan procurementPlan);

    MakueniPlanning createPlanning(ProcurementPlan procurementPlan, CabinetPaper paper);

    Unit createPlanningItemUnit(PlanItem item);

    Amount createPlanningItemUnitAmount(PlanItem item);

    Item createPlanningItem(PlanItem item);

    Classification createPlanningItemClassification(PlanItem item);

    Amount.Currency getCurrency();

    Set<Organization> createParties(Tender tender, Planning planning);

    Bids createBids(TenderQuotationEvaluation quotationEvaluation);

    Award createAward(ProfessionalOpinion professionalOpinion, AwardNotification awardNotification,
                      AwardAcceptance awardAcceptance);

    Contract createContract(org.devgateway.toolkit.persistence.dao.form.Contract contract);

    Release createRelease(PurchaseRequisition purchaseRequisition);
}
