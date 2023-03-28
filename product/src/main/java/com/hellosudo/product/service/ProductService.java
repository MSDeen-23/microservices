package com.hellosudo.product.service;

import com.hellosudo.product.model.ProductRequest;
import com.hellosudo.product.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long id);

    void reduceQuantity(long productId, long quantity);
}
