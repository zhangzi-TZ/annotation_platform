package com.annotationplatform.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateSessionRequest {
    @NotBlank(message = "Session name is required")
    @Size(max = 120, message = "Name must be 120 characters or fewer")
    private String name;

    @Size(max = 500, message = "Description must be 500 characters or fewer")
    private String description;

    public CreateSessionRequest() {
    }

    public CreateSessionRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
