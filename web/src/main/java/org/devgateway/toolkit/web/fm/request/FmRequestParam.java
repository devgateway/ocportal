package org.devgateway.toolkit.web.fm.request;

import org.devgateway.toolkit.web.fm.validator.ValidFmRequestParam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ValidFmRequestParam
public class FmRequestParam implements Serializable {
    private List<String> fmNames = new ArrayList<>();

    private List<String> fmPrefixes = new ArrayList<>();

    public List<String> getFmNames() {
        return fmNames;
    }

    public void setFmNames(List<String> fmNames) {
        this.fmNames = fmNames;
    }

    public List<String> getFmPrefixes() {
        return fmPrefixes;
    }

    public void setFmPrefixes(List<String> fmPrefixes) {
        this.fmPrefixes = fmPrefixes;
    }
    @Override
    public String toString() {
        return "FmRequestParam{fmNames=" + fmNames + ", fmPrefixes=" + fmPrefixes + "}";
    }
}
