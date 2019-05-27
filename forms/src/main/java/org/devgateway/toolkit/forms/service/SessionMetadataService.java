package org.devgateway.toolkit.forms.service;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

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
@Service
public class SessionMetadataService {


    @Autowired
    private ProcurementPlanService procurementPlanService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PurchaseRequisitionService purchaseRequisitionService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private FiscalYearService fiscalYearService;


    private void setSessionKey(MetaDataKey<Long> key, GenericPersistable persistable) {
        final Session session = Session.get();
        if (session != null && persistable != null) {
            session.setMetaData(key, persistable.getId());
        } else {
            if (session != null && persistable == null) {
                session.setMetaData(key, null);
            }
        }
    }

    private <S extends GenericPersistable & Serializable> S getSessionPersistable(MetaDataKey<Long> key,
                                                                                  BaseJpaService<S> service) {

        final Session session = Session.get();
        if (session != null) {
            final Long objId = session.getMetaData(key);
            if (objId != null) {
                return service.findById(objId).get();
            }
        }
        return null;
    }

    public void setSessionPP(final ProcurementPlan object) {
        setSessionKey(PROCUREMENT_PLAN, object);
    }


    public ProcurementPlan getSessionPP() {
        return getSessionPersistable(PROCUREMENT_PLAN, procurementPlanService);
    }

    public void setSessionProject(final Project object) {
        setSessionKey(PROJECT, object);
    }

    public Project getSessionProject() {
        return getSessionPersistable(PROJECT, projectService);
    }

    public void setSessionPurchaseRequisition(final PurchaseRequisition object) {
        setSessionKey(PURCHASE_REQUISITION, object);
    }

    public PurchaseRequisition getSessionPurchaseRequisition() {
        return getSessionPersistable(PURCHASE_REQUISITION, purchaseRequisitionService);
    }

    public void setSessionDepartment(final Department object) {
        setSessionKey(DEPARTMENT, object);
    }

    public Department getSessionDepartment() {
        return getSessionPersistable(DEPARTMENT, departmentService);
    }

    public void setSessionFiscalYear(final FiscalYear object) {
        setSessionKey(FISCAL_YEAR, object);
    }

    public FiscalYear getSessionFiscalYear() {
        return getSessionPersistable(FISCAL_YEAR, fiscalYearService);
    }

    public static void clearSessionData() {
        final Session session = Session.get();
        if (session != null) {
            ALL_SESSION_KEYS.forEach(key -> session.setMetaData(key, null));
        }
    }
}
