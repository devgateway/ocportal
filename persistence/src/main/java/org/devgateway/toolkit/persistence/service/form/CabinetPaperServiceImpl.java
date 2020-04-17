package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.repository.form.CabinetPaperRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gmutuhu
 *
 */
@Service
@Transactional
public class CabinetPaperServiceImpl extends AbstractMakueniEntityServiceImpl<CabinetPaper>
        implements CabinetPaperService {

    @Autowired
    private CabinetPaperRepository cabinetPaperRepository;

    @Override
    protected BaseJpaRepository<CabinetPaper, Long> repository() {
        return cabinetPaperRepository;
    }

    @Override
    public TextSearchableRepository<CabinetPaper, Long> textRepository() {
        return cabinetPaperRepository;
    }

    @Override
    public CabinetPaper newInstance() {
        return new CabinetPaper();
    }

    @Override
    public Long countByProcurementPlanAndNameAndIdNot(final ProcurementPlan procurementPlan, final String name,
            final Long id) {
        return cabinetPaperRepository.countByProcurementPlanAndNameAndIdNot(procurementPlan, name, id);
    }

    @Override
    public List<CabinetPaper> findByProcurementPlan(final ProcurementPlan procurementPlan) {
        return cabinetPaperRepository.findByProcurementPlan(procurementPlan);
    }


}
