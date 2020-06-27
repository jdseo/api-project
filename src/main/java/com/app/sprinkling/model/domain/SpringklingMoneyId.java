package com.app.sprinkling.model.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class SpringklingMoneyId implements Serializable {

    /**
     * Token
     */
    @EqualsAndHashCode.Include
    @Column(name = "TOKEN")
    private String token;

    /**
     * 뿌린 사용자 ID
     */
    @EqualsAndHashCode.Include
    @Column(name = "USER_ID")
    private Long userId;

    /**
     * 방 ID
     */
    @EqualsAndHashCode.Include
    @Column(name = "ROOM_ID")
    private String roomId;

    @Builder
    public SpringklingMoneyId(String token, Long userId, String roomId) {
        this.token = token;
        this.userId = userId;
        this.roomId = roomId;
    }

}
