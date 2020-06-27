package com.app.sprinkling.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
public class RandomGeneratorTest {

    @Test
    public void generateRandomString() {
        String randomString = RandomGenerator.generateRandomString(3, true, false);

        System.out.println("Random String: " + randomString);
        log.info("Random String: " + randomString);
    }

    @Test
    public void generateRandomAmount() {
        List<Long> randomAmountList = RandomGenerator.generateRandomAmount(5, 100_000);

        System.out.println("List: " + randomAmountList.toString());
        log.info("List: {}", randomAmountList.toString());
        System.out.println("Sum: " + randomAmountList.stream().reduce(Long::sum).get());
        log.info("Sum: {}", randomAmountList.stream().reduce(Long::sum).get());
    }

}
