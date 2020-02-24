package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.ProfessionalOpinionRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-04-24
 */
@Service
@Transactional
public class ProfessionalOpinionServiceImpl extends AbstractMakueniEntityServiceImpl<ProfessionalOpinion>
        implements ProfessionalOpinionService {

    @Autowired
    private ProfessionalOpinionRepository professionalOpinionRepository;

    @Override
    protected BaseJpaRepository<ProfessionalOpinion, Long> repository() {
        return professionalOpinionRepository;
    }

    @Override
    public ProfessionalOpinion newInstance() {
        return new ProfessionalOpinion();
    }

    @Override
    @Cacheable
    public ProfessionalOpinion findByTenderProcess(final TenderProcess tenderProcess) {
        return professionalOpinionRepository.findByTenderProcess(tenderProcess);
    }
}
