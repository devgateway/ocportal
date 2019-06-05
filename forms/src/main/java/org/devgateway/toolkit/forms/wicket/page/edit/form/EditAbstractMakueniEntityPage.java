package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.service.SessionMetadataService;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditStatusEntityPage;
import org.devgateway.toolkit.forms.wicket.page.overview.department.DepartmentOverviewPage;
import org.devgateway.toolkit.forms.wicket.styles.BaseStyles;
import org.devgateway.toolkit.persistence.dao.StatusChangedComment;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.service.form.AbstractMakueniEntityService;
import org.devgateway.toolkit.persistence.service.form.MakueniEntityServiceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.DRAFT;

/**
 * @author idobre
 * @since 2019-04-02
 */
public abstract class EditAbstractMakueniEntityPage<T extends AbstractMakueniEntity>
        extends AbstractEditStatusEntityPage<T> {

    protected ButtonContentModal revertToDraftModal;

    protected ModalSaveEditPageButton modalSaveEditPageButton;

    @SpringBean
    protected SessionMetadataService sessionMetadataService;

    @SpringBean
    protected MakueniEntityServiceResolver makeniEntityServiceResolver;

    protected static final Logger logger = LoggerFactory.getLogger(EditAbstractMakueniEntityPage.class);

    public EditAbstractMakueniEntityPage(final PageParameters parameters) {
        super(parameters);

        this.listPageClass = DepartmentOverviewPage.class;
    }

    @Override
    protected void onAfterRevertToDraft(AjaxRequestTarget target) {
        Collection<? extends AbstractMakueniEntity> allChildrenInHierarchy = getJpaService().getAllChildrenInHierarchy(
                editForm.getModelObject());
        allChildrenInHierarchy.stream().filter(c -> !c.getStatus().equals(DRAFT)).forEach(c -> {
            logger.info("Reverting to DRAFT " + c.getClass().getSimpleName());
            c.setNewStatusComment("Reverted to DRAFT because of related "
                    + editForm.getModelObject().getClass().getSimpleName() + " entity was reverted to DRAFT");
            final StatusChangedComment comment = new StatusChangedComment();
            comment.setStatus(DRAFT);
            comment.setComment(c.getNewStatusComment());
            c.getStatusComments().add(comment);
            c.setStatus(DRAFT);
            makeniEntityServiceResolver.saveAndFlushMakueniEntity(c);
        });
    }

    protected ButtonContentModal createRevertToDraftModal() {
        ButtonContentModal buttonContentModal = new ButtonContentModal(
                "revertToDraftModal",
                Model.of("Reverting to draft this entity will result in reverting to draft any other entities downstream. Proceed?"),
                Model.of("REVERT TO DRAFT"));
        return buttonContentModal;
    }


//    @Override
//    protected ModalSaveEditPageButton getRevertToDraftPageButton() {
//        final ModalSaveEditPageButton saveEditPageButton = new ModalSaveEditPageButton("revertToDraft",
//                new StringResourceModel("revertToDraft", this, null), revertToDraftModal) {
//            @Override
//            protected String getOnClickScript() {
//                return WebConstants.DISABLE_FORM_LEAVING_JS;
//            }
//
//            @Override
//            protected void onSubmit(final AjaxRequestTarget target) {
//                setStatusAppendComment(DBConstants.Status.DRAFT);
//                super.onSubmit(target);
//                target.add(editForm);
//                setButtonsPermissions();
//                onAfterRevertToDraft(target);
//            }
//        };
//        saveEditPageButton.setIconType(FontAwesomeIconType.thumbs_down);
//        return saveEditPageButton;
//    }



    public AbstractMakueniEntityService<T> getJpaService() {
        return (AbstractMakueniEntityService<T>) jpaService;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        enableDisableAutosaveFields(null);

//        revertToDraftModal = createRevertToDraftModal();
//        entityButtonsFragment.add(revertToDraftModal);

      //  revertToDraftModal.modalSavePageButton((ModalSaveEditPageButton) revertToDraftPageButton);

    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);

        response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
                BaseStyles.class, "assets/js/formLeavingHelper.js")));
    }
}
