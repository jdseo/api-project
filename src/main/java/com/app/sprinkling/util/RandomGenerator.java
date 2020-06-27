package com.app.sprinkling.util;

import com.app.sprinkling.exception.SpringklingMoneyApiException;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 랜덤 생성기
 */
public class RandomGenerator {

    /**
     * 랜덤 문자 생성
     *
     * @param length
     * @param useLetters
     * @param useNumbers
     * @return 랜덤 문자열
     */
    public static String generateRandomString(int length, boolean useLetters, boolean useNumbers) {
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    /**
     * 랜덤 지금액 생성
     *
     * @param count
     * @param amount
     * @return 랜덤 지금액 분배 리스트
     */
    public static List<Long> generateRandomAmount(long count, long amount) {
        long remainedAmount = amount;
        List<Long> randomAmountList = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            String random = RandomStringUtils.random(2, '0', '9', false, true);
            if (i != count){
                long randomAmount = remainedAmount * Long.parseLong(random) / 100;
                randomAmountList.add(randomAmount);

                remainedAmount -= randomAmount;
            } else {
                randomAmountList.add(remainedAmount);
            }
        }

        Long calcSum = randomAmountList.stream().reduce(Long::sum)
                .orElseThrow(() -> new SpringklingMoneyApiException("generateRandomAmount() 합계 오류"));

        if (calcSum != amount) {
            throw new SpringklingMoneyApiException("generateRandomAmount() 합계 불일치 오류");
        }

        return randomAmountList;
    }

}
