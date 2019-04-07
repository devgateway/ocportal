/**
 * 
 */
package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.ContractDocument;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Transactional
public interface ContractDocumentRepository extends CategoryRepository<ContractDocument> {

}
