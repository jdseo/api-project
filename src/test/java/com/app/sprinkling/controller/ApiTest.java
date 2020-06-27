package com.app.sprinkling.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
public class ApiTest {

    private final String url = "http://localhost:8080";

    @Test
    public void headerAndParameterTest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", "1");
        headers.set("X-ROOM-ID", "ROOM1");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> exchange = restTemplate.exchange(url + "/api/v1/springkling", HttpMethod.GET, entity, Map.class);

        Map body = exchange.getBody();
        log.info("result: {}", body);

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.set("X-USER-ID", "1");

        entity = new HttpEntity<>(headers);
        exchange = restTemplate.exchange(url + "/api/v1/springkling", HttpMethod.GET, entity, Map.class);

        body = exchange.getBody();
        log.info("result: {}", body);
    }

    @Test
    public void springklingAndGetInfo() throws JSONException {
        // springkling
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", "2000");
        headers.set("X-ROOM-ID", "ROOM2000");

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("count", 3);
        jsonParam.put("amount", 20_000);

        HttpEntity<String> entity = new HttpEntity<>(jsonParam.toString(), headers);
        ResponseEntity<Map> exchange = restTemplate.exchange(url + "/api/v1/springkling", HttpMethod.POST, entity, Map.class);

        Map springklingBody = exchange.getBody();
        log.info("result: {}", springklingBody);

        String token = (String) ((Map<String, Object>) springklingBody.get("data")).get("token");

        // GetInfo
        headers.set("X-USER-ID", "2000");
        headers.set("X-ROOM-ID", "ROOM2000");
        HttpEntity<String> getInfoEntity = new HttpEntity<>(headers);
        ResponseEntity<Map> getInfoExchange = restTemplate.exchange(url + "/api/v1/springkling?token=" + token, HttpMethod.GET, getInfoEntity, Map.class);

        Map body = getInfoExchange.getBody();
        log.info("result: {}", body);
    }

    @Test
    public void springklingAndReceive() throws JSONException {
        // springkling
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", "2000");
        headers.set("X-ROOM-ID", "ROOM2000");

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("count", 3);
        jsonParam.put("amount", 20_000);

        HttpEntity<String> entity = new HttpEntity<>(jsonParam.toString(), headers);
        ResponseEntity<Map> exchange = restTemplate.exchange(url + "/api/v1/springkling", HttpMethod.POST, entity, Map.class);

        Map springklingBody = exchange.getBody();
        log.info("result: {}", springklingBody);

        String token = (String) ((Map<String, Object>) springklingBody.get("data")).get("token");

        // receive
        headers.set("X-USER-ID", "3000");
        headers.set("X-ROOM-ID", "ROOM2000");

        jsonParam = new JSONObject();
        jsonParam.put("token", token);
        HttpEntity<String> receiveEntity = new HttpEntity<>(jsonParam.toString(), headers);
        ResponseEntity<Map> receiveExchange = restTemplate.exchange(url + "/api/v1/springkling", HttpMethod.PUT, receiveEntity, Map.class);

        Map body = receiveExchange.getBody();
        log.info("result: {}", body);
    }

}
