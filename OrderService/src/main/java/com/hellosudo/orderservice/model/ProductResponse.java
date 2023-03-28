package com.hellosudo.orderservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductResponse {
    private long productId;
    private String productName;
    private long price;
    private long quantity;
}
