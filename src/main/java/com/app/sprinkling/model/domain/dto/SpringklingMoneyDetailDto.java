package com.app.sprinkling.model.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpringklingMoneyDetailDto {

    /**
     * 뿌린 금액
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long receivedAmount;

    /**
     * 받은 사용자
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long receivedUserId;

    /**
     * 받은 일자
     */
    @JsonIgnore
    private LocalDateTime receivedAt;
}
