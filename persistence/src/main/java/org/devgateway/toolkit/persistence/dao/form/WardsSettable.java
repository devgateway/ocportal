package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.dao.categories.Ward;

import java.util.List;

/**
 * @author mpostelnicu
 */
public interface WardsSettable {
    void setWards(List<Ward> wards);
}
