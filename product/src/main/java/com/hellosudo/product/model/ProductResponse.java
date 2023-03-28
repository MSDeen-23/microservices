package com.hellosudo.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
