package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.dao.AbstractChildAuditableEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author idobre
 * @since 2019-04-05
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "parent_id")})
public class PlanItem extends AbstractChildAuditableEntity<ProcurementPlan> {

}
