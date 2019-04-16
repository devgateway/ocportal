package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.repository.form.CabinetPaperRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */

@Service
@Transactional(readOnly = true)
public class CabinetPaperServiceImpl extends BaseJpaServiceImpl<CabinetPaper> implements CabinetPaperService {

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

}
