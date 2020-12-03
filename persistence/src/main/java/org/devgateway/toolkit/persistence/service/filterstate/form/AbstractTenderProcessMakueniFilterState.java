package org.devgateway.toolkit.persistence.service.filterstate.form;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity_;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan_;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.Project_;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess_;
import org.devgateway.toolkit.persistence.dao.form.Tender_;
import org.devgateway.toolkit.persistence.service.filterstate.StatusAuditableEntityFilterState;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-05-23
 */

public abstract class AbstractTenderProcessMakueniFilterState<T extends AbstractTenderProcessMakueniEntity>
        extends StatusAuditableEntityFilterState<T> {
    private TenderProcess tenderProcess;
    private String tenderTitle;

    @Override
    public Specification<T> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(root.get(AbstractTenderProcessMakueniEntity_.tenderProcess).isNotNull());
            
            if (StringUtils.isNotBlank(tenderTitle)) {
                predicates.add(cb.like(
                        cb.lower(root.join(AbstractTenderProcessMakueniEntity_.tenderProcess)
                                .join(TenderProcess_.tender).get(Tender_.tenderTitle)),
                        "%" + tenderTitle.toLowerCase() + "%"
                ));
            }

            if (tenderProcess != null) {
                final Project project = tenderProcess.getProject();

                if (project != null) {
                    final ProcurementPlan procurementPlan = project.getProcurementPlan();

                    if (procurementPlan != null) {
                        if (procurementPlan.getDepartment() != null) {
                            predicates.add(cb.equal(root.get(AbstractTenderProcessMakueniEntity_.tenderProcess)
                                    .get(TenderProcess_.project).get(Project_.procurementPlan)
                                    .get(ProcurementPlan_.department), procurementPlan.getDepartment()));
                        }

                        if (procurementPlan.getFiscalYear() != null) {
                            predicates.add(cb.equal(root.get(AbstractTenderProcessMakueniEntity_.tenderProcess)
                                    .get(TenderProcess_.project).get(Project_.procurementPlan)
                                    .get(ProcurementPlan_.fiscalYear), procurementPlan.getFiscalYear()));
                        }
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

