package org.devgateway.toolkit.persistence.dao;

/**
 * @author idobre
 * @since 2019-04-14
 */
public interface ListViewItem {
    Boolean getEditable();

    void setEditable(Boolean editable);

    Boolean getExpanded();

    void setExpanded(Boolean expanded);
}
