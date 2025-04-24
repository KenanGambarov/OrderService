package com.orderservice.client;

import com.orderservice.client.decoder.ClientServiceErrorDecoder;
import com.orderservice.dto.response.StockResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stock-service", url = "${client.stock-service.url}",
        path = "/internal/v1",configuration = ClientServiceErrorDecoder.class)
public interface StockServiceClient {


    @GetMapping("/stock/{id}")
    StockResponseDto getProductById(@PathVariable("id") Long id);

}
