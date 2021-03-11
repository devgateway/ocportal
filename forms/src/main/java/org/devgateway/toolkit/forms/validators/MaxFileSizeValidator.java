package org.devgateway.toolkit.forms.validators;

import org.apache.wicket.ThreadContext;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dto.FileSize;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;

/**
 * @author Nadejda Mandrescu
 */
public class MaxFileSizeValidator implements IValidator<Collection<FileMetadata>> {
    private static final long serialVersionUID = -4386023418740650823L;

    private FormComponent formComponent;
    private FileSize maxFileSize;
    private long maxFileSizeBytes;

    public MaxFileSizeValidator(final FileSize maxFileSize, final FormComponent formComponent) {
        this.maxFileSizeBytes = maxFileSize.toBytes();
        this.maxFileSize = maxFileSize.toHighest();
        this.formComponent = formComponent;
    }

    @Override
    public void validate(final IValidatable<Collection<FileMetadata>> validatable) {
        NumberFormat formatter = getNumberFormat();

        validatable.getValue().stream().filter(m -> m.getSize() > maxFileSizeBytes).forEach(m -> {
            String unitStr = formComponent.getString("fileUnit." + maxFileSize.getUnit().getName());
            String valueStr = formatter.format(maxFileSize.getValue());

            ValidationError error = new ValidationError(this);
            error.setVariable("fileName", m.getName());
            error.setVariable("maxFileSize", valueStr);
            error.setVariable("sizeUnit", unitStr);
            validatable.error(error);
        });
    }

    private NumberFormat getNumberFormat() {
        NumberFormat formatter = DecimalFormat.getInstance(ThreadContext.getSession().getLocale());
        formatter.setMaximumFractionDigits(1);
        formatter.setMinimumFractionDigits(0);
        return formatter;
    }
}
