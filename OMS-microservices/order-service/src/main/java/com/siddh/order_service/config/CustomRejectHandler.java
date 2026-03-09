package com.siddh.order_service.config;

import com.siddh.order_service.exception.SystemOverloadException;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class CustomRejectHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        throw new SystemOverloadException(
                "The Trading Engine is currently at maximum capacity. Please try placing your order again in a few seconds."
        );
    }
}
