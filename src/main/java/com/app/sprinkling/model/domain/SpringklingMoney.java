package com.app.sprinkling.model.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 돈 뿌리기 엔티티 (MASTER)
 */
@Entity
@Table(name = "SPRK_MONEY_M")
@Slf4j
@Data
@NoArgsConstructor
public class SpringklingMoney implements Serializable {

    @EmbeddedId
    private SpringklingMoneyId springklingMoneyId;

    /**
     * 상태
     *  - pending
     *  - complete
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 뿌린 대상자 수
     */
    @Column(name = "USER_CNT")
    private Long userCount;

    /**
     * 뿌린 금액
     */
    @Column(name = "TOTAL_AMT")
    private Long totalAmount;

    /**
     * 생성일자
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "springklingMoney", cascade = CascadeType.ALL)
    private List<SpringklingMoneyDetail> springklingMoneyList = new ArrayList<>();

    @Builder
    public SpringklingMoney(SpringklingMoneyId springklingMoneyId, Long userCount, Long totalAmount) {
        this.springklingMoneyId = springklingMoneyId;
        this.userCount = userCount;
        this.totalAmount = totalAmount;

        // default 설정
        this.status = "pending";
        this.createdAt = LocalDateTime.now();
    }

    public boolean alreadyReceived(Long userId) {
        for (SpringklingMoneyDetail detail : springklingMoneyList) {
            if ("complete".equalsIgnoreCase(detail.getStatus()) &&
                    userId.longValue() == detail.getReceiveUserId().longValue()) {
                return true;
            }
        }

        return false;
    }

    public Long receive(Long userId) {
        for (SpringklingMoneyDetail detail : springklingMoneyList) {
            if ("pending".equalsIgnoreCase(detail.getStatus())) {
                detail.setReceiveUserId(userId);
                detail.setReceivedAt(LocalDateTime.now());
                detail.setStatus("complete");

                return detail.getAmount();
            }
        }

        return null;
    }

    public boolean isReceiveAll() {
        for (SpringklingMoneyDetail detail : springklingMoneyList) {
            if ("pending".equalsIgnoreCase(detail.getStatus())) {
                return false;
            }
        }

        return true;
    }
}
