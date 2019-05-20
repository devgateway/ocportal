package org.devgateway.toolkit.forms.wicket.components.util;

import org.apache.wicket.Session;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;

import static org.devgateway.toolkit.forms.WebConstants.ALL_SESSION_KEYS;
import static org.devgateway.toolkit.forms.WebConstants.DEPARTMENT;
import static org.devgateway.toolkit.forms.WebConstants.FISCAL_YEAR;
import static org.devgateway.toolkit.forms.WebConstants.PROCUREMENT_PLAN;
import static org.devgateway.toolkit.forms.WebConstants.PROJECT;
import static org.devgateway.toolkit.forms.WebConstants.PURCHASE_REQUISITION;

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
            session.setMetaData(PROCUREMENT_PLAN, object);
        }
    }

    public static ProcurementPlan getSessionPP() {
        final Session session = Session.get();
        if (session != null) {
            final ProcurementPlan procurementPlan = session.getMetaData(PROCUREMENT_PLAN);
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
            session.setMetaData(FISCAL_YEAR, object);
        }
    }

    public static FiscalYear getSessionFiscalYear() {
        final Session session = Session.get();
        if (session != null) {
            final FiscalYear fiscalYear = session.getMetaData(FISCAL_YEAR);
            return fiscalYear;
        }

        return null;
    }

    public static void clearSessionData() {
        final Session session = Session.get();
        if (session != null) {
            ALL_SESSION_KEYS.forEach(key -> session.setMetaData(key, null));
        }
    }
}
