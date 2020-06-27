package com.app.sprinkling.model.web;

import lombok.Data;

/**
 * Rest 요청 공통 클래스
 */
@Data
public class SpringklingParam {

    /**
     * X-USER-ID (숫자)
     */
    private Long userId;

    /**
     * X-ROOM-ID (문자)
     */
    private String roomId;

    /**
     * 뿌리기 Token (문자)
     */
    private String token;

    /**
     * 분배 대상자 수 (숫자)
     */
    private Long count;

    /**
     * 분배 금액 (숫자)
     */
    private Long amount;

}
