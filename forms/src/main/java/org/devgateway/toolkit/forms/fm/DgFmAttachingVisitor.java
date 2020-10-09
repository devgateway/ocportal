package org.devgateway.toolkit.forms.fm;

import org.apache.wicket.Component;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * Visitor that can be added into a {@link DgFmComponentSubject} that was previously attached, and that
 * contains unattached {@link DgFmComponentSubject}S as children. These children will be automatically
 * attached to the Feature Manager. see {@link org.devgateway.toolkit.web.fm.DgFmSubject#attachFm(String)}
 *
 * @author mihai 
 * 
 */
public class DgFmAttachingVisitor implements IVisitor<Component, Void> {

    @Override
    public void component(Component object, IVisit<Void> visit) {
        if (object instanceof DgFmComponentSubject && ((DgFmComponentSubject) object).getFmName() == null) {
            ((DgFmComponentSubject) object).attachFm();
        }
    }
}
