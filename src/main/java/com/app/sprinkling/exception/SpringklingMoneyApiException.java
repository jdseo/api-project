package com.app.sprinkling.exception;

public class SpringklingMoneyApiException extends RuntimeException {

    public SpringklingMoneyApiException() {
        super();
    }

    public SpringklingMoneyApiException(String message) {
        super(message);
    }

    public SpringklingMoneyApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpringklingMoneyApiException(Throwable cause) {
        super(cause);
    }

}
