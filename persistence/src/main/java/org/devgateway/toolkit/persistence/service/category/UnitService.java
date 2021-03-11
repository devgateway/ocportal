package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Unit;

/**
 * @author idobre
 * @since 26/08/2019
 */
public interface UnitService extends CategoryService<Unit> {


    Unit findByLabel(String label);

    Unit findByLabelIgnoreCase(String label);
}
