package com.siddh.order_service.exception;

public class SystemOverloadException extends RuntimeException{
    public SystemOverloadException(String message) {
        super(message);
    }
}
