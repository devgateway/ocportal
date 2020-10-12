package org.devgateway.toolkit.persistence.dto;

import org.devgateway.toolkit.persistence.validator.groups.NonDraft;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class PMCNotesOffline implements Serializable {
    private Long id;

    @NotEmpty(groups = NonDraft.class)
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
