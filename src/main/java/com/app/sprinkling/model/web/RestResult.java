package com.app.sprinkling.model.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Rest 응답 공통 클래스
 */
@Data
@NoArgsConstructor
public class RestResult {

    private boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMessage;

    @Builder
    public RestResult(boolean success, Object data, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
    }
    
}
