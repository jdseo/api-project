package com.app.sprinkling.model.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 돈뿌린 상세내역 DTO
 */
@Data
public class SpringklingMoneyDto {

    /**
     * Token
     */
    @JsonIgnore
    private String token;

    /**
     * 사용자 ID
     */
    @JsonIgnore
    private Long userId;

    /**
     * 방 ID
     */
    @JsonIgnore
    private String roomId;

    /**
     * 돈뿌리기 상태
     */
    @JsonIgnore
    private String status;

    /**
     * 뿌릴 대상자 수
     */
    @JsonIgnore
    private Long userCount;

    /**
     * 뿌린 시각
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    /**
     * 뿌린 금액
     */
    private Long totalAmount;

    /**
     * 받기 완료된 금액
     */
    private Long totalReceivedAmount;

    /**
     * 받기완료된 내역
     */
    private List<SpringklingMoneyDetailDto> springklingMoneyDetailList = new ArrayList<>();

    public Long getTotalReceivedAmount() {
        if (this.springklingMoneyDetailList.size() != 0) {
            Long sum = springklingMoneyDetailList
                    .stream()
                    .map(x -> x.getReceivedAmount())
                    .reduce(0L, Long::sum);
            return sum;
        } else {
            return 0L;
        }
    }

}
