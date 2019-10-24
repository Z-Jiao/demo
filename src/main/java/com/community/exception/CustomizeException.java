package com.community.exception;

public class CustomizeException extends RuntimeException {
    private String message;

    public CustomizeException(String message) {
        this.message = message;
    }

    public CustomizeException(ICustomizeErroeCode erroeCode) {
        this.message = erroeCode.getMessage();
    }


}
