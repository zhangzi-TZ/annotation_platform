package com.annotationplatform.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class CreateIdentityRequest {
    @NotBlank(message = "label is required")
    private String label;

    @NotEmpty(message = "occurrences are required")
    private List<@Valid IdentityOccurrenceRequest> occurrences;

    public CreateIdentityRequest() {
    }

    public CreateIdentityRequest(String label, List<IdentityOccurrenceRequest> occurrences) {
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
