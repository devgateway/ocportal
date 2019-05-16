package org.devgateway.toolkit.forms.wicket.components.util;

import org.apache.wicket.Session;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;

import static org.devgateway.toolkit.forms.WebConstants.DEPARTMENT;
import static org.devgateway.toolkit.forms.WebConstants.FISCALYEAR;
import static org.devgateway.toolkit.forms.WebConstants.PROCUREMENTPLAN;
import static org.devgateway.toolkit.forms.WebConstants.PROJECT;
import static org.devgateway.toolkit.forms.WebConstants.PURCHASE_REQUISITION;
import static org.devgateway.toolkit.forms.WebConstants.TENDER;
import static org.devgateway.toolkit.forms.WebConstants.TENDER_EVALUATION;

/**
 * @author idobre
 * @since 2019-05-15
 */
public final class SessionUtil {
    private SessionUtil() {

    }

    public static void setSessionPP(final ProcurementPlan object) {
        final Session session = Session.get();
        if (session != null) {
            session.setMetaData(PROCUREMENTPLAN, object);
        }
    }

    public static ProcurementPlan getSessionPP() {
        final Session session = Session.get();
        if (session != null) {
            final ProcurementPlan procurementPlan = session.getMetaData(PROCUREMENTPLAN);
            return procurementPlan;
        }

        return null;
    }

    public static void setSessionProject(final Project object) {
        final Session session = Session.get();
        if (session != null) {
            session.setMetaData(PROJECT, object);
        }
    }

    public static Project getSessionProject() {
        final Session session = Session.get();
        if (session != null) {
            final Project project = session.getMetaData(PROJECT);
            return project;
        }

        return null;
    }

    public static void setSessionPurchaseRequisition(final PurchaseRequisition object) {
        final Session session = Session.get();
        if (session != null) {
            session.setMetaData(PURCHASE_REQUISITION, object);
        }
    }

    public static PurchaseRequisition getSessionPurchaseRequisition() {
        final Session session = Session.get();
        if (session != null) {
            final PurchaseRequisition purchaseRequisition = session.getMetaData(PURCHASE_REQUISITION);
            return purchaseRequisition;
        }

        return null;
    }

    public static void setSessionTender(final Tender object) {
        final Session session = Session.get();
        if (session != null) {
            session.setMetaData(TENDER, object);
        }
    }

    public static Tender getSessionTender() {
        final Session session = Session.get();
        if (session != null) {
            final Tender tender = session.getMetaData(TENDER);
            return tender;
        }

        return null;
    }

    public static void setSessionTenderQuotationEvaluation(final TenderQuotationEvaluation object) {
        final Session session = Session.get();
        if (session != null) {
            session.setMetaData(TENDER_EVALUATION, object);
        }
    }

    public static TenderQuotationEvaluation getSessionTenderQuotationEvaluation() {
        final Session session = Session.get();
        if (session != null) {
            final TenderQuotationEvaluation tenderQuotationEvaluation = session.getMetaData(TENDER_EVALUATION);
            return tenderQuotationEvaluation;
        }

        return null;
    }

    public static void setSessionDepartment(final Department object) {
        final Session session = Session.get();
        if (session != null) {
            session.setMetaData(DEPARTMENT, object);
        }
    }

    public static Department getSessionDepartment() {
        final Session session = Session.get();
        if (session != null) {
            final Department department = session.getMetaData(DEPARTMENT);
            return department;
        }

        return null;
    }

    public static void setSessionFiscalYear(final FiscalYear object) {
        final Session session = Session.get();
        if (session != null) {
            session.setMetaData(FISCALYEAR, object);
        }
    }

    public static FiscalYear getSessionFiscalYear() {
        final Session session = Session.get();
        if (session != null) {
            final FiscalYear fiscalYear = session.getMetaData(FISCALYEAR);
            return fiscalYear;
        }

        return null;
    }
}
