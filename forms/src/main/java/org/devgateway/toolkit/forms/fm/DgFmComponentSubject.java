package org.devgateway.toolkit.forms.fm;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.devgateway.toolkit.web.fm.DgFmSubject;
import org.devgateway.toolkit.web.fm.entity.DgFeature;

/**
 * Interface that can be attached to wicket {@link Component} to provide FM mechanism
 *
 */
public interface DgFmComponentSubject extends DgFmSubject {

    MetaDataKey<String> FM_NAME_KEY = new MetaDataKey<String>() {
        private static final long serialVersionUID = 1L;
    };

    MetaDataKey<Boolean> FM_NO_AUTO_ATTACH_KEY = new MetaDataKey<Boolean>() {
        private static final long serialVersionUID = 1L;
    };

    /**
     * @return The fm name, saved into {@link Component}'s metadata
     */
    @Override
    default String getFmName() {
        return ((Component) this).getMetaData(FM_NAME_KEY);
    }

    /**
     * Mark this component not to auto attach to FM.
     * @see #attachFm()
     */
    default void fmNoAutoAttach() {
        ((Component) this).setMetaData(FM_NO_AUTO_ATTACH_KEY, true);
    }

    /**
     * Invoked to disable FM bahavior for this component
     * @return
     */
    default Boolean isNoAutoAttach() {
        return Boolean.TRUE.equals(((Component) this).getMetaData(FM_NO_AUTO_ATTACH_KEY));
    }

    /**
     * Save FM name as internal {@link Component} metadata
     * @param fmName
     */
    @Override
    default void setFmName(String fmName) {
        ((Component) this).setMetaData(FM_NAME_KEY, fmName);
    }

    /**
     * Get the FM name of the parent {@link Component} if the component is a {@link DgFmSubject}
     * @param parent
     * @return
     */
    default String getParentFmName(Component parent) {
        if (parent instanceof DgFmSubject) {
            return ((DgFmSubject) parent).getFmName();
        }
        if (parent == null) {
            return null;
        }
        return getParentFmName(parent.getParent());
    }

    /**
     * Attaches the FM to the current {@link Component} by using the component hierarchy and markup IDs to
     * construct the {@link DgFeature#getName()}
     */
    default void attachFm() {
        if (isNoAutoAttach() || isFmAttached()) {
            return;
        }
        String parentCombinedFmName = getFmService().getParentCombinedFmName(getParentFmName(((Component) this)
                .getParent()), ((Component) this).getId());
        if (parentCombinedFmName == null) {
            return;
        }
        attachFm(parentCombinedFmName);
    }

    /**
     * Alternative way to attach FM behavior to children components.
     * {@link DgFmAttachingVisitor} is preferred
     * @param children
     */
    default void attachFmForChildren(Component... children) {
        for (Component c : children) {
            if (c instanceof DgFmComponentSubject && ((DgFmComponentSubject) c).getFmName() == null) {
                ((DgFmComponentSubject) c).attachFm();
            }
        }
    }
}
