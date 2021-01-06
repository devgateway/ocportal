package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.Statusable;

public class DeptOverviewStatusLabel extends Label {

    public DeptOverviewStatusLabel(final String id, String status) {
        super(id, new Model<>(""));

        add(AttributeAppender.append("class", status !=null
                ? status.toLowerCase() : DBConstants.Status.NOT_STARTED.toLowerCase()));

        add(new TooltipBehavior(Model.of(status != null ? status.toLowerCase() : "")));
    }
}
