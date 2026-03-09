package com.siddh.order_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<Object> handleInvalidOrderException(InvalidOrderException ex){
        Map<String,Object> errorBody=new LinkedHashMap<>();

        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("Status", HttpStatus.BAD_REQUEST.value());
        errorBody.put("error","Bad Request");
        errorBody.put("message",ex.getMessage());

        return new ResponseEntity<>(errorBody,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SystemOverloadException.class)
    public ResponseEntity<Object>handleSystemOverload(SystemOverloadException ex){

        Map<String,Object> errorBody=new LinkedHashMap<>();
        errorBody.put("timestamp",LocalDateTime.now());
        errorBody.put("status",HttpStatus.SERVICE_UNAVAILABLE.value());
        errorBody.put("error","Service Unavailable");
        errorBody.put("message",ex.getMessage());

        return new ResponseEntity<>(errorBody,HttpStatus.SERVICE_UNAVAILABLE);
    }

    //generic exception for all exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object>handleGeneralException(Exception ex){
        Map<String,Object>errorBody=new LinkedHashMap<>();

        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("Status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorBody.put("error","Internal Server Error");
        errorBody.put("message", "Unexpected error: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        return new ResponseEntity<>(errorBody,HttpStatus.BAD_REQUEST);
    }

}
