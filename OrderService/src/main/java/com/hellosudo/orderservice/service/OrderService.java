package com.hellosudo.orderservice.service;

import com.hellosudo.orderservice.model.OrderRequest;
import com.hellosudo.orderservice.external.response.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
