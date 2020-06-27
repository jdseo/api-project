package com.app.sprinkling.exception.handler;

import com.app.sprinkling.exception.SpringklingMoneyApiException;
import com.app.sprinkling.model.web.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;

@EnableWebMvc
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            Exception.class,
            RuntimeException.class,
            HttpRequestMethodNotSupportedException.class,
            SpringklingMoneyApiException.class
    })
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RestResult apiException(HttpServletRequest request, Exception e) {
        log.error("Global API Exception (ApiException) 발생", e);

        return RestResult.builder()
                .success(false)
                .errorMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestResult noHandlerFoundException(HttpServletRequest request, Exception e) {
        log.error("Global API Exception (NoHandlerFoundException) 발생", e);

        return RestResult.builder()
                .success(false)
                .errorMessage("유효하지 않은 요청(URI)입니다.")
                .build();
    }

}