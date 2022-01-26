package com.fsoft.quizsystem.object.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@Builder
@JsonPropertyOrder({"status_code", "message", "timestamp", "errors"})
public class ErrorResponse {

    @JsonProperty("status_code")
    private int responseCode;

    private String message;

    private final Date timestamp = new Date();

    private Map<String, String> errors;
}
