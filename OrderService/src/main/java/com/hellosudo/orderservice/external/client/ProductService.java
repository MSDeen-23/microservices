package com.hellosudo.orderservice.external.client;

import com.hellosudo.orderservice.exception.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CircuitBreaker(name="external",fallbackMethod = "fallback")
@FeignClient(name = "product",url = "${microservices.product}")
public interface ProductService {

    @PutMapping("/reduceQuantity/{id}")
    ResponseEntity<Void> reduceQuantity(@PathVariable("id") long productId,@RequestParam long quantity);

    default ResponseEntity<Void> fallback(Exception exception){
        throw new CustomException("Product service is not avvailable","UNAVAILABLE",500);
    }
}
