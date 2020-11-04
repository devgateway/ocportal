package org.devgateway.toolkit.web.fm.validator;

import org.devgateway.toolkit.web.fm.request.FmRequestParam;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FmRequestParamValidator
        implements ConstraintValidator<ValidFmRequestParam, FmRequestParam> {

    @Override
    public void initialize(ValidFmRequestParam constraintAnnotation) {
    }

    @Override
    public boolean isValid(FmRequestParam frp, ConstraintValidatorContext context) {
        if (!ObjectUtils.isEmpty(frp.getFmPrefixes())
                && frp.getFmPrefixes().stream().anyMatch(s -> s.length() < 3)) {
            return false;
        }
        return (!ObjectUtils.isEmpty(frp.getFmNames()) || !ObjectUtils.isEmpty(frp.getFmPrefixes()))
                && ((ObjectUtils.isEmpty(frp.getFmNames())) || ObjectUtils.isEmpty(frp.getFmPrefixes()));
    }
}