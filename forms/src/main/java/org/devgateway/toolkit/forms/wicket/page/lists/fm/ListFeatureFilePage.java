package org.devgateway.toolkit.forms.wicket.page.lists.fm;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.list.BootstrapListView;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.fileinput.BootstrapFileInput;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.fileinput.FileInputConfig;
import org.apache.commons.io.IOUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.handler.resource.ResourceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CharSequenceResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.persistence.fm.entity.FeatureConfig;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.devgateway.toolkit.web.Constants;
import org.devgateway.toolkit.web.security.SecurityConstants;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
public class ListFeatureFilePage extends BasePage {

    @SpringBean
    private DgFmService dgFmService;

    /**
     * Construct.
     *
     * @param parameters current page parameters
     */
    public ListFeatureFilePage(PageParameters parameters) {
        super(parameters);

        IModel<List<FeatureConfig>> featureConfigs = new LoadableDetachableModel<List<FeatureConfig>>() {
            @Override
            protected List<FeatureConfig> load() {
                return new ArrayList<>(dgFmService.getFeatureConfigs());
            }
        };

        add(new BootstrapListView<FeatureConfig>("featureConfigs", featureConfigs) {

            @Override
            protected void populateItem(ListItem<FeatureConfig> item) {
                FeatureConfig featureConfig = item.getModelObject();

                item.setOutputMarkupId(true);

                item.add(new Label("resourceLocation", featureConfig.getResourceLocation()));

                item.add(new BootstrapLink<Void>("download", Buttons.Type.Link) {
                    @Override
                    public void onClick() {
                        FeatureConfig fc = item.getModelObject();

                        IResource resource = new CharSequenceResource(
                                Constants.ContentType.YAML_UTF8, fc.getContent(), fc.getResourceLocation());

                        getRequestCycle().scheduleRequestHandlerAfterCurrent(
                                new ResourceRequestHandler(resource, null));
                    }
                }.setLabel(new ResourceModel("download")));

                IModel<List<FileUpload>> uploadsModel = Model.ofList(new ArrayList<>());

                FileInputConfig config = new FileInputConfig();
                config.maxFileCount(1);
                item.add(new BootstrapFileInput("upload", uploadsModel, config) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target) {
                        super.onSubmit(target);

                        List<FileUpload> uploads = uploadsModel.getObject();
                        if (uploads.size() == 1) {
                            try {
                                FileUpload upload = uploads.get(0);

                                String content = IOUtils.toString(upload.getInputStream(), StandardCharsets.UTF_8);

                                dgFmService.addOrReplaceFeatureConfig(
                                        new FeatureConfig(featureConfig.getResourceLocation(), content));
                            } catch (IOException | RuntimeException e) {
                                error("Failed to update the configuration. Cause: " + e.getMessage());
                                target.add(feedbackPanel);
                            }
                        }
                    }
                });
            }
        }.setReuseItems(true));
    }
}
