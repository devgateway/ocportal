package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.dao.categories.Category_;
import org.devgateway.toolkit.persistence.repository.category.CategoryRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.metamodel.SingularAttribute;

/**
 * @author mpostelnicu
 */
public abstract class CategoryServiceImpl<T extends Category> extends BaseJpaServiceImpl<T>
        implements CategoryService<T> {

    @Override
    public Page<T> findByLabel(final String label, final Pageable page) {
        return categoryRepository().findByLabel(label, page);
    }

    public CategoryRepository<T> categoryRepository() {
        return (CategoryRepository<T>) repository();
    }

    public T findFirstByLabel(final String label) {
        return categoryRepository().findFirstByLabel(label);
    }

    @Override
    public SingularAttribute<? super T, String> getTextAttribute() {
        return Category_.label;
    }
}
