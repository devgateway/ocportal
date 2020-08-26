package org.devgateway.toolkit.persistence.dto;

import java.io.Serializable;

public class PMCNotesOffline implements Serializable {
    private Long id;
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
