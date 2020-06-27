package com.app.sprinkling.resolver;

import com.app.sprinkling.exception.SpringklingMoneyApiException;
import com.app.sprinkling.model.web.SpringklingParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Enumeration;
import java.util.Map;

@Slf4j
public class RequestParamArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String JSON_BODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return SpringklingParam.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        if (request.getHeader("X-USER-ID") == null) {
            throw new InvalidParameterException("X-USER-ID 헤더는 필수 입력입니다.");
        }

        if (request.getHeader("X-ROOM-ID") == null) {
            throw new InvalidParameterException("X-ROOM-ID 헤더는 필수 입력입니다.");
        }

        // parameter 맵 생성
        Map<String, Object> requestParamMap = new ConcurrentHashMap();
        requestParamMap.put("userId", request.getHeader("X-USER-ID"));
        requestParamMap.put("roomId", request.getHeader("X-ROOM-ID"));

        // body 맵핑
        ObjectMapper mapper = new ObjectMapper();
        String body = getRequestBody(webRequest);
        if (StringUtils.isNotEmpty(body)) {
            Map<String, Object> map = mapper.readValue(body, Map.class);
            requestParamMap.putAll(map);
        }

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);

            requestParamMap.put(name, value);
        }

        // parameter 맵 변환
        SpringklingParam springklingParam = mapper.convertValue(requestParamMap, SpringklingParam.class);

        return springklingParam;
    }

    private String getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String body = (String) webRequest.getAttribute(JSON_BODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);

        if (body == null) {
            try {
                body = IOUtils.toString(servletRequest.getInputStream());
                webRequest.setAttribute(JSON_BODY_ATTRIBUTE, body, NativeWebRequest.SCOPE_REQUEST);
                return body;
            } catch (IOException e) {
                log.error("Internal Server Error", e);
                throw new SpringklingMoneyApiException("Internal Server Error", e);
            }
        }

        return body;
    }

}
