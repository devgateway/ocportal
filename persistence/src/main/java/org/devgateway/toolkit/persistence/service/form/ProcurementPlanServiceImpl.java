package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.Department_;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear_;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan_;
import org.devgateway.toolkit.persistence.repository.form.ProcurementPlanRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.util.stream.Stream;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Service
@Transactional
public class ProcurementPlanServiceImpl extends AbstractMakueniEntityServiceImpl<ProcurementPlan>
        implements ProcurementPlanService {
    @Autowired
    private ProcurementPlanRepository procurementPlanRepository;

    @Override
    public Long countByDepartmentAndFiscalYear(final Department department, final FiscalYear fiscalYear) {
        return procurementPlanRepository.countByDepartmentAndFiscalYear(department, fiscalYear);
    }

    @Override
    public Long countByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear, Long exceptId) {
        return procurementPlanRepository.countByDepartmentAndFiscalYearExceptId(department, fiscalYear, exceptId);
    }

    @Override
    public Stream<ProcurementPlan> findAllStream() {
        return procurementPlanRepository.findAllStream();
    }

    @Override
    protected BaseJpaRepository<ProcurementPlan, Long> repository() {
        return procurementPlanRepository;
    }

    @Override
    public ProcurementPlan newInstance() {
        return new ProcurementPlan();
    }

    @Override
    public ProcurementPlan findByDepartmentAndFiscalYear(final Department department, final FiscalYear fiscalYear) {
        return procurementPlanRepository.findByDepartmentAndFiscalYear(department, fiscalYear);
    }

    @Override
    public SingularAttribute<? super ProcurementPlan, String> getTextAttribute() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Specification<ProcurementPlan> getTextSpecification(String text) {
        return (root, query, cb) ->
                cb.or(
                        cb.like(cb.lower(root.join(ProcurementPlan_.department).get(Department_.label)), "%"
                                + text.toLowerCase() + "%"),
                        cb.like(cb.lower(root.join(ProcurementPlan_.fiscalYear).get(FiscalYear_.name)), "%"
                                + text.toLowerCase() + "%")
                );
    }
}
