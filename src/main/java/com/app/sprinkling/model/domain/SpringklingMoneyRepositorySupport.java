package com.app.sprinkling.model.domain;

import com.app.sprinkling.model.domain.dto.SpringklingMoneyDetailDto;
import com.app.sprinkling.model.domain.dto.SpringklingMoneyDto;
import com.app.sprinkling.model.web.SpringklingParam;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app.sprinkling.model.domain.QSpringklingMoney.springklingMoney;
import static com.app.sprinkling.model.domain.QSpringklingMoneyDetail.springklingMoneyDetail;

@Repository
public class SpringklingMoneyRepositorySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public SpringklingMoneyRepositorySupport(JPAQueryFactory queryFactory) {
        super(SpringklingMoney.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 뿌리기 내역 (지금내역 포함) 조회
     *
     * @param springklingParam
     * @return SpringklingMoneyDto 뿌리기 내역
     */
    public SpringklingMoneyDto findAllSpringklingMoneyDetail(SpringklingParam springklingParam) {
        QueryResults<Tuple> query = queryFactory
                .select(
                        springklingMoney.springklingMoneyId.token,
                        springklingMoney.springklingMoneyId.userId,
                        springklingMoney.springklingMoneyId.roomId,
                        springklingMoney.totalAmount,
                        springklingMoney.userCount,
                        springklingMoney.status,
                        springklingMoney.createdAt,
                        springklingMoneyDetail.status,
                        springklingMoneyDetail.amount,
                        springklingMoneyDetail.receiveUserId,
                        springklingMoneyDetail.receivedAt
                )
                .from(springklingMoney)
                .leftJoin(springklingMoneyDetail).on(
                                springklingMoneyDetail.token.eq(springklingMoney.springklingMoneyId.token)
                                .and(springklingMoneyDetail.userId.eq(springklingMoney.springklingMoneyId.userId)
                                .and(springklingMoneyDetail.roomId.eq(springklingMoney.springklingMoneyId.roomId))
                                .and(springklingMoneyDetail.status.equalsIgnoreCase("complete")))
                )
                .where(springklingMoney.springklingMoneyId.token.eq(springklingParam.getToken())
                        .and(springklingMoney.springklingMoneyId.userId.eq(springklingParam.getUserId()))
                        .and(springklingMoney.springklingMoneyId.roomId.eq(springklingParam.getRoomId()))
                )
                .fetchResults();

        List<Tuple> queryResult = query.getResults();

        // 조회 결과가 없을때
        if (queryResult == null || queryResult.isEmpty()) {
            return null;
        }

        SpringklingMoneyDto springklingMoneyDto = new SpringklingMoneyDto();
        springklingMoneyDto.setToken(queryResult.get(0).get(springklingMoney.springklingMoneyId.token));
        springklingMoneyDto.setUserId(queryResult.get(0).get(springklingMoney.springklingMoneyId.userId));
        springklingMoneyDto.setRoomId(queryResult.get(0).get(springklingMoney.springklingMoneyId.roomId));
        springklingMoneyDto.setStatus(queryResult.get(0).get(springklingMoney.status));
        springklingMoneyDto.setTotalAmount(queryResult.get(0).get(springklingMoney.totalAmount));
        springklingMoneyDto.setUserCount(queryResult.get(0).get(springklingMoney.userCount));
        springklingMoneyDto.setCreatedAt(queryResult.get(0).get(springklingMoney.createdAt));

        for (Tuple item : queryResult) {
            if (item.get(springklingMoneyDetail.amount) != null) {
                SpringklingMoneyDetailDto springklingMoneyDetailDto = new SpringklingMoneyDetailDto();
                springklingMoneyDetailDto.setReceivedAmount(item.get(springklingMoneyDetail.amount));
                springklingMoneyDetailDto.setReceivedUserId(item.get(springklingMoneyDetail.receiveUserId));
                springklingMoneyDetailDto.setReceivedAt(item.get(springklingMoneyDetail.receivedAt));

                springklingMoneyDto.getSpringklingMoneyDetailList().add(springklingMoneyDetailDto);
            }
        }

        return springklingMoneyDto;
    }

    /**
     * 뿌리기 내역 단건 조회
     *
     * @param springklingParam
     * @return SpringklingMoney 뿌리기 내역
     */
    public SpringklingMoney findOneToReceive(SpringklingParam springklingParam) {
        BooleanExpression condition = springklingMoney.springklingMoneyId.token.eq(springklingParam.getToken())
                .and(springklingMoney.springklingMoneyId.roomId.eq(springklingParam.getRoomId()));

        SpringklingMoney springklingMoney =
                (SpringklingMoney) queryFactory.from(QSpringklingMoney.springklingMoney)
                        .where(condition)
                        .fetchOne();

        return springklingMoney;
    }

}
