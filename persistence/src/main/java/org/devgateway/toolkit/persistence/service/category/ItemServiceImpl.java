/**
 * 
 */
package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.devgateway.toolkit.persistence.repository.category.ItemRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Service
@Transactional(readOnly = true)
public class ItemServiceImpl  extends CategoryServiceImpl<Item> implements ItemService {

    @Autowired
    private ItemRepository repository;

    @Override
    protected BaseJpaRepository<Item, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<Item, Long> textRepository() {
        return repository;
    }

    @Override
    public Item newInstance() {
        return new Item();
    }
}
