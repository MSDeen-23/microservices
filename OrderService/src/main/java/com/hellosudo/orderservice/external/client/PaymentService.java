package com.hellosudo.orderservice.external.client;

import com.hellosudo.orderservice.exception.CustomException;
import com.hellosudo.orderservice.external.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name="external",fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/api/v1/payment")
public interface PaymentService {
    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    default void fallback(Exception exception){
        throw new CustomException("Payment service is not avvailable","UNAVAILABLE",500);
    }
}
