package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.devgateway.toolkit.persistence.repository.category.ChargeAccountRepository;
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
public class ChargeAccountServiceImpl extends BaseJpaServiceImpl<ChargeAccount> implements ChargeAccountService {

    @Autowired
    private ChargeAccountRepository repository;

    @Override
    public ChargeAccount newInstance() {
        return new ChargeAccount();
    }

    @Override
    public TextSearchableRepository<ChargeAccount, Long> textRepository() {
        return repository;
    }

    @Override
    protected BaseJpaRepository<ChargeAccount, Long> repository() {
        return repository;
    }
}
