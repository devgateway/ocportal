package org.devgateway.toolkit.forms.fm;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.util.lang.Args;
import org.devgateway.toolkit.persistence.fm.DgFmSubject;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;

/**
 * @author mpostelnicu
 * Behavior that attaches FM functionality to non FM wicket components, like links etc...
 */
public class DgFmBehavior extends Behavior implements DgFmComponentSubject {
    /**
     * the component that this handler is bound to.
     */
    protected Component component;
    protected String fmName;
    protected DgFmComponentSubject fmNameSubject;

    public DgFmBehavior(String fmName) {
        super();
        this.fmName = fmName;
    }

    /**
     * Inherits the fmName from the given {@link DgFmComponentSubject} such as other component
     *
     * @param fmNameSubject
     */
    public DgFmBehavior(DgFmComponentSubject fmNameSubject) {
        super();
        this.fmNameSubject = fmNameSubject;
    }

    public DgFmBehavior() {
        super();
    }

    @Override
    public void onConfigure(Component component) {
        if (fmNameSubject != null) {
            fmNameSubject.attachFm();
            attachFm(fmNameSubject.getFmName());
        } else if (fmName == null) {
            attachFm();
        } else {
            attachFm(fmName);
        }
        component.setEnabled(isFmEnabled(component::isEnabled));
        component.setVisible(isFmVisible(component::isVisible));
    }

    @Override
    public Component asComponent() {
        return component;
    }

    @Override
    public final void bind(final Component hostComponent) {
        Args.notNull(hostComponent, "hostComponent");

        if (component != null) {
            throw new IllegalStateException("One component per behavior possible!");
        }

        component = hostComponent;
    }

    /**
     * If not bound by given {@link #fmNameSubject}, this will find a parent that can be used
     * to retrieve the fmService
     *
     * @param component
     * @return
     * @see #getFmService()
     */
    protected DgFmSubject findFmSubjectParent(Component component) {
        if (component == null) {
            return null;
        }
        if (component instanceof DgFmSubject) {
            return (DgFmSubject) component;
        }
        return findFmSubjectParent(component.getParent());
    }


    @Override
    public DgFmService getFmService() {
        return fmNameSubject == null ? findFmSubjectParent(component).getFmService()
                : fmNameSubject.getFmService();
    }

}
