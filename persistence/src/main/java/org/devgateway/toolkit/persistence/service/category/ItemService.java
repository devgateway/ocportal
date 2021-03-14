/**
 * 
 */
package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Item;

/**
 * @author gmutuhu
 *
 */
public interface ItemService extends CategoryService<Item> {

    Item findByCode(String code);
}
