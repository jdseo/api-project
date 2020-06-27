package com.app.sprinkling.service;

import com.app.sprinkling.model.domain.dto.SpringklingMoneyDto;
import com.app.sprinkling.model.web.SpringklingParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringklingMoneyServiceTest {

    @Autowired
    private SpringklingMoneyService springklingMoneyService;

    @Test
    public void springklingAndGetInfo() {
        SpringklingParam sender = new SpringklingParam();
        sender.setUserId(1L);
        sender.setRoomId("ROOM1");
        sender.setCount(5L);
        sender.setAmount(50_000L);

        String token = springklingMoneyService.springkling(sender);
        log.info("========== token: {} ===========", token);

        // 토큰 설정
        sender.setToken(token);

        SpringklingMoneyDto info = springklingMoneyService.getInfo(sender);
        log.info("========== Springkling Info: {} ===========", info);
    }

    @Test
    public void springklingAndReceiveWithSameUser() {
        SpringklingParam sender = new SpringklingParam();
        sender.setUserId(1L);
        sender.setRoomId("ROOM1");
        sender.setCount(5L);
        sender.setAmount(50_000L);

        String token = springklingMoneyService.springkling(sender);
        log.info("========== token: {} ===========", token);

        // 토큰 설정
        sender.setToken(token);
        long receiveAmount = springklingMoneyService.receive(sender);
    }

    @Test
    public void springklingAndReceiveWithOtherUser() {
        SpringklingParam sender = new SpringklingParam();
        sender.setUserId(1L);
        sender.setRoomId("ROOM1");
        sender.setCount(5L);
        sender.setAmount(50_000L);

        String token = springklingMoneyService.springkling(sender);
        log.info("========== token: {} ===========", token);

        // 토큰 설정
        sender.setToken(token);

        SpringklingParam receiver = new SpringklingParam();
        receiver.setUserId(2L);
        receiver.setRoomId("ROOM1");
        receiver.setToken(token);
        long receiveAmount = springklingMoneyService.receive(receiver);

        log.info("========== Receive Amount: {} ===========", receiveAmount);

        SpringklingMoneyDto info = springklingMoneyService.getInfo(sender);
        log.info("========== Springkling Info: {} ===========", info);
    }

    @Test
    public void receiveTwiceSameUser() {
        SpringklingParam sender = new SpringklingParam();
        sender.setUserId(1L);
        sender.setRoomId("ROOM1");
        sender.setCount(5L);
        sender.setAmount(50_000L);

        String token = springklingMoneyService.springkling(sender);
        log.info("========== token: {} ===========", token);

        // 토큰 설정
        sender.setToken(token);

        SpringklingParam receiver = new SpringklingParam();
        receiver.setUserId(2L);
        receiver.setRoomId("ROOM1");
        receiver.setToken(token);

        // 첫번째 받기
        long receiveAmount = springklingMoneyService.receive(receiver);
        log.info("========== Receive Amount: {} ===========", receiveAmount);

        // 두번째 받기
        receiveAmount = springklingMoneyService.receive(receiver);
        log.info("========== Receive Amount: {} ===========", receiveAmount);
    }

    @Test
    public void expiredGetInfoWithOwner() {
        // data.sql 초기 데이터 사용
        SpringklingParam sender = new SpringklingParam();
        sender.setUserId(1000L);
        sender.setRoomId("ROOM1000");
        sender.setToken("GOD");

        SpringklingMoneyDto info = springklingMoneyService.getInfo(sender);
        log.info("========== Springkling Info: {} ===========", info);
    }

    @Test
    public void expiredReceive() {
        // data.sql 초기 데이터 사용
        SpringklingParam receiver = new SpringklingParam();
        receiver.setUserId(2L);
        receiver.setRoomId("ROOM1000");
        receiver.setToken("GOD");

        long receiveAmount = springklingMoneyService.receive(receiver);
        log.info("========== Receive Amount: {} ===========", receiveAmount);
    }
}
