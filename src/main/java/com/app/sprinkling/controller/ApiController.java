package com.app.sprinkling.controller;

import com.app.sprinkling.model.domain.dto.SpringklingMoneyDto;
import com.app.sprinkling.model.web.RestResult;
import com.app.sprinkling.model.web.SpringklingParam;
import com.app.sprinkling.service.SpringklingMoneyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiController {

    private final SpringklingMoneyService springklingMoneyService;

    /**
     * 돈 뿌리기
     *
     * @return 생성된 token
     */
    @PostMapping("/springkling")
    public RestResult springkling(SpringklingParam springklingParam) {
        String token = springklingMoneyService.springkling(springklingParam);

        return RestResult.builder()
                .success(true)
                .data(new HashMap<String, String>() {{ put("token", token); }})
                .build();
    }

    /**
     * 돈 받기
     *
     * @return 받은 금액
     */
    @PutMapping("/springkling")
    public RestResult receive(SpringklingParam springklingParam) {
        long amount = springklingMoneyService.receive(springklingParam);

        return RestResult.builder()
                .success(true)
                .data(new HashMap<String, Object>() {{ put("amount", amount); }})
                .build();
    }

    /**
     * 조회
     *
     * @return 돈뿌린 상세내역
     */
    @GetMapping("/springkling")
    public RestResult getInfo(SpringklingParam springklingParam) {
        SpringklingMoneyDto springklingMoneyDto = springklingMoneyService.getInfo(springklingParam);

        return RestResult.builder()
                .success(true)
                .data(springklingMoneyDto)
                .build();
    }

}
