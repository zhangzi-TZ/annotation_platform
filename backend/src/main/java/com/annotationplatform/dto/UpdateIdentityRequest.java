package com.annotationplatform.dto;

import javax.validation.Valid;
import java.util.List;

public class UpdateIdentityRequest {
    private String label;
    private List<@Valid IdentityOccurrenceRequest> occurrences;

    public UpdateIdentityRequest() {
    }

    public UpdateIdentityRequest(String label, List<IdentityOccurrenceRequest> occurrences) {
        this.label = label;
        this.occurrences = occurrences;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<IdentityOccurrenceRequest> getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(List<IdentityOccurrenceRequest> occurrences) {
        this.occurrences = occurrences;
    }
}
