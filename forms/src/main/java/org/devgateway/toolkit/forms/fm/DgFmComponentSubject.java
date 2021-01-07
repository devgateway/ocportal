package org.devgateway.toolkit.forms.fm;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.devgateway.toolkit.persistence.fm.DgFmSubject;
import org.devgateway.toolkit.persistence.fm.entity.DgFeature;

/**
 * Interface that can be attached to wicket {@link Component} to provide FM mechanism
 *
 */
public interface DgFmComponentSubject extends DgFmSubject {

    MetaDataKey<String> FM_NAME_KEY = new MetaDataKey<String>() {
    };

    MetaDataKey<Boolean> FM_NO_AUTO_ATTACH_KEY = new MetaDataKey<Boolean>() {
    };

    /**
     * @return The fm name, saved into {@link Component}'s metadata
     */
    @Override
    default String getFmName() {
        return asComponent().getMetaData(FM_NAME_KEY);
    }

    default Component asComponent() {
        return (Component) this;
    }

    /**
     * Mark this component not to auto attach to FM.
     *
     * @see #attachFm()
     */
    default void fmNoAutoAttach() {
        asComponent().setMetaData(FM_NO_AUTO_ATTACH_KEY, true);
    }

    /**
     * Invoked to disable FM bahavior for this component
     * @return
     */
    default Boolean isNoAutoAttach() {
        return Boolean.TRUE.equals(asComponent().getMetaData(FM_NO_AUTO_ATTACH_KEY));
    }

    /**
     * Save FM name as internal {@link Component} metadata
     * @param fmName
     */
    @Override
    default void setFmName(String fmName) {
        asComponent().setMetaData(FM_NAME_KEY, fmName);
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
        attachWithParentFm(asComponent().getId());
    }

    default void attachWithParentFm(String fmName) {
        if (isNoAutoAttach() || isFmAttached()) {
            return;
        }
        String parentCombinedFmName = getParentCombinedFmName(asComponent().getParent(), fmName);
        if (parentCombinedFmName == null) {
            return;
        }
        attachFm(parentCombinedFmName);
    }

    default String getParentCombinedFmName(Component parent, String fmName) {
        return getFmService().getParentCombinedFmName(getParentFmName(parent), fmName);
    }

    /**
     * Alternative way to attach FM behavior to children components.
     * {@link DgFmAttachingVisitor} is preferred
     *
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
