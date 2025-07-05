package com.ecommerce.product.service;


import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        mapProductRequestToProductEntity(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mapSavedProductToProductResponse(savedProduct);
    }

    private ProductResponse mapSavedProductToProductResponse(Product savedProduct) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProduct.getId());
        productResponse.setName(savedProduct.getName());
        productResponse.setDescription(savedProduct.getDescription());
        productResponse.setPrice(savedProduct.getPrice());
        productResponse.setCategory(savedProduct.getCategory());
        productResponse.setActive(savedProduct.getActive());
        productResponse.setStockQuantity(savedProduct.getStockQuantity());
        productResponse.setImageUrl(savedProduct.getImageUrl());
        return productResponse;
    }

    private void mapProductRequestToProductEntity(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setImageUrl(productRequest.getImageUrl());
    }


    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
     return productRepository.findById(id)
             .map(existingProduct -> {
                 mapProductRequestToProductEntity(existingProduct, productRequest);
                 Product savedProduct = productRepository.save(existingProduct);
                 return mapSavedProductToProductResponse(savedProduct);
             });
    }


    public List<ProductResponse> getProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapSavedProductToProductResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapSavedProductToProductResponse);
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);

    }

    public List<ProductResponse> searchProducts(String keyword) {
    return productRepository.searchProducts(keyword).stream()
            .map(this::mapSavedProductToProductResponse)
            .collect(Collectors.toList());
    }
}
