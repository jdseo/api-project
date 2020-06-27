package com.app.sprinkling.model.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 돈 뿌리기 상세 엔티티 (DETAIL)
 */
@Entity
@Table(name = "SPRK_MONEY_D")
@Slf4j
@Data
@NoArgsConstructor
public class SpringklingMoneyDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    /**
     * Token
     */
    @Column(name = "TOKEN")
    private String token;

    /**
     * 뿌린 사용자 ID
     */
    @Column(name = "USER_ID")
    private Long userId;

    /**
     * 방 ID
     */
    @Column(name = "ROOM_ID")
    private String roomId;

    /**
     * 상태
     *  - pending
     *  - complete
     *  - expired
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 뿌린 금액 (받는(을) 금액)
     */
    @Column(name = "AMOUNT")
    private Long amount;

    /**
     * 받은 사용자 ID
     */
    @Column(name = "RECV_USER_ID")
    private Long receiveUserId;

    /**
     * 받은 일자
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "RECV_AT")
    private LocalDateTime receivedAt;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name="TOKEN", referencedColumnName="TOKEN", insertable=false, updatable=false),
            @JoinColumn(name="USER_ID", referencedColumnName="USER_ID", insertable=false, updatable=false),
            @JoinColumn(name="ROOM_ID", referencedColumnName="ROOM_ID", insertable=false, updatable=false)
    })
    private SpringklingMoney springklingMoney;

    @Builder
    public SpringklingMoneyDetail(String token, Long userId, String roomId, SpringklingMoney springklingMoney) {
        this.token = token;
        this.userId = userId;
        this.roomId = roomId;
        this.springklingMoney = springklingMoney;

        // default 값 설정
        this.status = "pending";
    }

}
