package com.siddh.analytics_server.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ORDER-SERVICE")
public class OrderClient {
}
