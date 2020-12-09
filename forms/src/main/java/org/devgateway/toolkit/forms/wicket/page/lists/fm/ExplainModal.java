package org.devgateway.toolkit.forms.wicket.page.lists.fm;

import static java.util.stream.Collectors.joining;

import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.devgateway.toolkit.persistence.fm.entity.DgFeature;

/**
 * @author Octavian Ciubotaru
 */
public class ExplainModal extends Modal<DgFeature> {

    public ExplainModal(String id, IModel<DgFeature> model) {
        super(id, model);

        header(new ResourceModel("explainModal.header"));
        size(Size.Large);
        setFadeIn(false);

        add(new Label("content", (IModel<String>) () -> {
            DgFeature dgFeature = ExplainModal.this.getModelObject();
            if (dgFeature == null) {
                return "";
            }
            return "<pre>" + dgFeature.explain()
                    .stream()
                    .map(l -> (l.isGray() ? "<span class=\"text-muted\">" : "<span>") + l.getText() + "</span>")
                    .collect(joining("<br>")) + "</pre>";
        }).setEscapeModelStrings(false));
    }
}
