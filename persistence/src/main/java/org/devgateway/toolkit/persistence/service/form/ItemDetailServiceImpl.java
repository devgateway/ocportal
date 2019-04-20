package org.devgateway.toolkit.persistence.service.form;


import org.devgateway.toolkit.persistence.dao.form.ItemDetail;
import org.devgateway.toolkit.persistence.repository.form.ItemDetailRepository;
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
public class ItemDetailServiceImpl extends BaseJpaServiceImpl<ItemDetail> implements ItemDetailService {

    @Autowired
    private ItemDetailRepository itemDetailRepository;

    @Override
    protected BaseJpaRepository<ItemDetail, Long> repository() {
        return itemDetailRepository;
    }

    @Override
    public ItemDetail newInstance() {
        return new ItemDetail();
    }

    @Override
    public TextSearchableRepository<ItemDetail, Long> textRepository() {
        return itemDetailRepository;
    }

}
