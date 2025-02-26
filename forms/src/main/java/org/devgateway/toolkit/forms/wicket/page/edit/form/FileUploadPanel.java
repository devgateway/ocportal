package org.devgateway.toolkit.forms.wicket.page.edit.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.form.FormGroup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

public class FileUploadPanel extends Panel {
    private static final long serialVersionUID = 1L;

    private IModel<List<FileUpload>> filesModel;
    private FileUploadField fileUploadField;

    public FileUploadPanel(String id) {
        super(id);
        filesModel = Model.ofList(new ArrayList<>());

        // Title
        add(new Label("title", "Procurement Plan Documents"));

        // Pending Files Label
        Label pendingFilesLabel = new Label("pendingFilesLabel", Model.of("Pending files (will be uploaded when the form is saved)"));
        pendingFilesLabel.setEscapeModelStrings(false);
        add(pendingFilesLabel);

        // File Upload Input
        FormGroup formGroup = new FormGroup("fileUploadGroup");
        fileUploadField = new FileUploadField("fileUploadField", filesModel);
        formGroup.add(fileUploadField);
        add(formGroup);

        // Upload Button
        AjaxButton uploadButton = new AjaxButton("uploadButton") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                List<FileUpload> uploadedFiles = fileUploadField.getFileUploads();
                if (uploadedFiles != null) {
                    filesModel.getObject().addAll(uploadedFiles);
                }
                target.add(FileUploadPanel.this);
            }
        };
        add(uploadButton);
    }

    public IModel<List<FileUpload>> getFilesModel() {
        return filesModel;
    }
}
