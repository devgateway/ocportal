package org.devgateway.toolkit.forms.wicket.page.overview.department;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.Statusable;

public class DeptOverviewStatusLabel extends Label {

    public DeptOverviewStatusLabel(final String id, final Statusable statusable) {
        super(id);

        add(AttributeAppender.append("class", statusable != null
                ? statusable.getStatus().toLowerCase() : DBConstants.Status.NOT_STARTED.toLowerCase()));
    }
}
