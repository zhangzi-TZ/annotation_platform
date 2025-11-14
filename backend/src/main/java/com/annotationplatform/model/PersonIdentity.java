package com.annotationplatform.model;

import java.util.ArrayList;
import java.util.List;

public class PersonIdentity {
    private String id;
    private String label;
    private String color;
    private List<IdentityOccurrence> occurrences;

    public PersonIdentity() {
        this.occurrences = new ArrayList<>();
    }

    public PersonIdentity(String id,
                          String label,
                          String color,
                          List<IdentityOccurrence> occurrences) {
        this.id = id;
        this.label = label;
        this.color = color;
        setOccurrences(occurrences);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<IdentityOccurrence> getOccurrences() {
        if (occurrences == null) {
            occurrences = new ArrayList<>();
        }
        return occurrences;
    }

    public void setOccurrences(List<IdentityOccurrence> occurrences) {
        this.occurrences = occurrences != null ? occurrences : new ArrayList<>();
    }
}
