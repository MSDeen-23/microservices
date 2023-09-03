package com.sudo.productservice.service;

import com.sudo.productservice.dto.ProductRequest;
import com.sudo.productservice.dto.ProductResponse;
import com.sudo.productservice.model.Product;
import com.sudo.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public long createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        Product savedProduct = productRepository.save(product);
        log.info("Product {} is saved ",product.getId());
        return savedProduct.getId();

    }

    public ProductResponse getProductById(Long id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()) {
            return mapToProductResponse(product.get());
        }
        return null;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
