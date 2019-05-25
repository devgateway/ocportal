/**
 * 
 */
package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.ContractDocumentType;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Transactional
public interface ContractDocumentTypeRepository extends CategoryRepository<ContractDocumentType> {

}
