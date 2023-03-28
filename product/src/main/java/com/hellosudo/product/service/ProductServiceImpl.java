package com.hellosudo.product.service;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.hellosudo.product.entity.Product;
import com.hellosudo.product.exception.ProductServiceCustomException;
import com.hellosudo.product.model.ProductRequest;
import com.hellosudo.product.model.ProductResponse;
import com.hellosudo.product.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.*;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product...");
        Product product = Product.builder()
                .productName(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();
        productRepository.save(product);
        log.info("Product Created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long id) {
        log.info("Getting product of id "+id);
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductServiceCustomException("Product with given id not found","PRODUCT_NOT_FOUND"));
        ProductResponse productResponse = new ProductResponse();
        copyProperties(product,productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity {} for Id: {}",quantity,productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductServiceCustomException("Product with   given id not found","PRODUCT_NOT_FOUND"));
        if(product.getQuantity()<quantity){
            throw new ProductServiceCustomException("Product does not have sufficient quantity","INSUFFICIENT_QUANTITY");
        }
        product.setQuantity(product.getQuantity()-quantity);
        productRepository.save(product);
        log.info("Product quantity updated successfully");
    }
}
