/*******************************************************************************
 * Copyright (c) 2021 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.persistence.dao.categories;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;

/**
 * @author Octavian Ciubotaru
 */
@Entity
@Audited
public class InspectionReportOutcome extends Category {
}