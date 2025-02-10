package org.devgateway.toolkit.persistence.service.filterstate.form;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessClientEntity;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessClientEntity_;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess_;
import org.devgateway.toolkit.persistence.dao.form.Tender_;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan_;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.filterstate.StatusAuditableEntityFilterState;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-05-23
 */

public abstract class AbstractTenderProcessClientFilterState<T extends AbstractTenderProcessClientEntity>
        extends StatusAuditableEntityFilterState<T> {
    private TenderProcess tenderProcess;
    private String tenderTitle;

    @Override
    public Specification<T> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(root.get(AbstractTenderProcessClientEntity_.tenderProcess).isNotNull());

            if (StringUtils.isNotBlank(tenderTitle)) {
                predicates.add(cb.like(
                        cb.lower(root.join(AbstractTenderProcessClientEntity_.tenderProcess)
                                .join(TenderProcess_.tender).get(Tender_.tenderTitle)),
                        "%" + tenderTitle.toLowerCase() + "%"
                ));
            }

            if (tenderProcess != null) {
                final ProcurementPlan procurementPlan = tenderProcess.getProcurementPlan();

                if (procurementPlan != null) {
                    Path<ProcurementPlan> pp = root.join(AbstractTenderProcessClientEntity_.tenderProcess)
                            .join(TenderProcess_.procurementPlan);

                    Department department = procurementPlan.getDepartment();
                    if (department != null) {
                        predicates.add(cb.equal(pp.get(ProcurementPlan_.department), department));
                    }

                    FiscalYear fiscalYear = procurementPlan.getFiscalYear();
                    if (fiscalYear != null) {
                        predicates.add(cb.equal(pp.get(ProcurementPlan_.fiscalYear), fiscalYear));
                    }
                }
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public TenderProcess getTenderProcess() {
        return tenderProcess;
    }

    public void setTenderProcess(final TenderProcess tenderProcess) {
        this.tenderProcess = tenderProcess;
    }

    public String getTenderTitle() {
        return tenderTitle;
    }

    public void setTenderTitle(String tenderTitle) {
        this.tenderTitle = tenderTitle;
    }
}

