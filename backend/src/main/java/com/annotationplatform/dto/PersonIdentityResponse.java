package com.annotationplatform.dto;

import java.util.List;

public class PersonIdentityResponse {
    private String id;
    private String label;
    private String color;
    private List<IdentityOccurrenceResponse> occurrences;

    public PersonIdentityResponse() {
    }

    public PersonIdentityResponse(String id,
                                  String label,
                                  String color,
                                  List<IdentityOccurrenceResponse> occurrences) {
        this.id = id;
        this.label = label;
        this.color = color;
        this.occurrences = occurrences;
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

    public List<IdentityOccurrenceResponse> getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(List<IdentityOccurrenceResponse> occurrences) {
        this.occurrences = occurrences;
    }
}
