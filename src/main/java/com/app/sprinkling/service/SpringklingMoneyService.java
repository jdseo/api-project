package com.app.sprinkling.service;

import com.app.sprinkling.exception.SpringklingMoneyApiException;
import com.app.sprinkling.model.domain.*;
import com.app.sprinkling.model.domain.dto.SpringklingMoneyDto;
import com.app.sprinkling.model.web.SpringklingParam;
import com.app.sprinkling.util.RandomGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpringklingMoneyService {

    private final SpringklingMoneyRepository springklingMoneyRepository;

    private final SpringklingMoneyRepositorySupport springklingMoneyRepositorySupport;

    private static final int RANDOM_STR_LENGTH = 3;

    private static final int RECEIVE_EXPIRE_MINUTES = 10;

    private static final int SEARCH_EXPIRE_DAYS = 7;

    @Transactional
    public String springkling(SpringklingParam springklingParam) {

        if (springklingParam.getCount() == null || springklingParam.getAmount() == null) {
            throw new InvalidParameterException("count, amount 는 필수 입력입니다.");
        }

        // SpringklingMoney 엔티티 생성
        SpringklingMoneyId springklingMoneyId = SpringklingMoneyId.builder()
                .token(RandomGenerator.generateRandomString(RANDOM_STR_LENGTH, true, false))
                .userId(springklingParam.getUserId())
                .roomId(springklingParam.getRoomId())
                .build();

        SpringklingMoney springklingMoney = SpringklingMoney.builder()
                .springklingMoneyId(springklingMoneyId)
                .totalAmount(springklingParam.getAmount())
                .userCount(springklingParam.getCount())
                .build();

        // 뿌릴 금액 랜덤 생성
        List<Long> randomAmount = RandomGenerator.generateRandomAmount(springklingParam.getCount(), springklingParam.getAmount());

        // SpringklingMoneyDetail 엔티티 생성
        for (int i = 0; i < springklingMoney.getUserCount(); i++) {
            SpringklingMoneyDetail detail = SpringklingMoneyDetail.builder()
                    .token(springklingMoney.getSpringklingMoneyId().getToken())
                    .userId(springklingMoney.getSpringklingMoneyId().getUserId())
                    .roomId(springklingMoney.getSpringklingMoneyId().getRoomId())
                    .springklingMoney(springklingMoney)
                    .build();

            // 랜덤 생성된 뿌릴 금액 지정
            detail.setAmount(randomAmount.get(i));

            springklingMoney.getSpringklingMoneyList().add(detail);
        }

        try {
            springklingMoneyRepository.save(springklingMoney);
        } catch (Exception e) {
            log.error("Server Error : 뿌리기 오류", e);
            throw new SpringklingMoneyApiException("Internal Server Error : 뿌리기 오류", e);
        }

        return springklingMoney.getSpringklingMoneyId().getToken();
    }

    @Transactional
    public long receive(SpringklingParam springklingParam) {
        if (springklingParam.getToken() == null) {
            throw new InvalidParameterException("token 은 필수 입력입니다.");
        }

        // 뿌리기 내역 조회
        SpringklingMoney springklingMoney =
                springklingMoneyRepositorySupport.findOneToReceive(springklingParam);

        // token 과 roomId 로 일치하는 내역이 없는 경우
        if (springklingMoney == null) {
            throw new SpringklingMoneyApiException("유효하지 않은 요청입니다.");
        }

        // 뿌리기 한 사용자가 받기 요청한 경우
        if (springklingMoney.getSpringklingMoneyId().getUserId() == springklingParam.getUserId()) {
            throw new SpringklingMoneyApiException("뿌리기한 사용자는 받을 수 없습니다.");
        }

        // 받기 유효기간 확인 (10분)
        if (LocalDateTime.now().minusMinutes(RECEIVE_EXPIRE_MINUTES).isAfter(springklingMoney.getCreatedAt())) {
            throw new SpringklingMoneyApiException("유효기간이 만료되었습니다.");
        }

        // 받은 적이 있는지 확인
        if (springklingMoney.alreadyReceived(springklingParam.getUserId())) {
            throw new SpringklingMoneyApiException("이미 받은 사용자 입니다.");
        }

        // 받기 완료 체크
        if (springklingMoney.isReceiveAll()) {
            throw new SpringklingMoneyApiException("받기가 종료 되었습니다.");
        }

        try {
            Long receiveAmount = springklingMoney.receive(springklingParam.getUserId());

            // 받기가 완료되었는지 확인
            if (springklingMoney.isReceiveAll()) {
                springklingMoney.setStatus("complete");
            }

            springklingMoneyRepository.save(springklingMoney);

            return receiveAmount;
        } catch (Exception e) {
            log.error("Internal Server Error : 받기 오류", e);
            throw new SpringklingMoneyApiException("Internal Server Error : 받기 오류", e);
        }
    }

    public SpringklingMoneyDto getInfo(SpringklingParam springklingParam) {

        if (springklingParam.getToken() == null) {
            throw new InvalidParameterException("token 은 필수 입력입니다.");
        }

        // 조회
        SpringklingMoneyDto springklingMoneyDto =
                springklingMoneyRepositorySupport.findAllSpringklingMoneyDetail(springklingParam);

        if (springklingMoneyDto == null) {
            throw new SpringklingMoneyApiException("조회 결과가 없습니다.");
        }

        // 조회 유효기간 확인 (7일)
        if (LocalDateTime.now().minusDays(SEARCH_EXPIRE_DAYS).isAfter(springklingMoneyDto.getCreatedAt())) {
            throw new SpringklingMoneyApiException("유효기간이 만료되었습니다.");
        }

        return springklingMoneyDto;
    }

}
