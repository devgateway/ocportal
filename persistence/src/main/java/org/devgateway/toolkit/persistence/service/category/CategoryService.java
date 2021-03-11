package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService<T extends Category> extends BaseJpaService<T>, TextSearchableService<T> {

    Page<T> findByLabel(String label, Pageable page);

    T findFirstByLabel(String label);
}
