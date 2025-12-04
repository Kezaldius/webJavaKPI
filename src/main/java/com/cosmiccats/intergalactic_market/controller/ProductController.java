package com.cosmiccats.intergalactic_market.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.cosmiccats.intergalactic_market.aop.RequiresFeatureToggle;
import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.dto.*;
import com.cosmiccats.intergalactic_market.mapper.ProductMapper;
import com.cosmiccats.intergalactic_market.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @RequiresFeatureToggle("cosmoCats")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductRequest requestDTO) {
        Product productToCreate = productMapper.toDomain(requestDTO);
        Product createdProduct = productService.createProduct(productToCreate);
        ProductDTO responseDto = productMapper.toDto(createdProduct);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @RequiresFeatureToggle("cosmoCats")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                    @Valid @RequestBody ProductRequest requestDTO) {
        Product productDetails = productMapper.toDomain(requestDTO);
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(productMapper.toDto(updatedProduct));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponse(responseCode = "204", description = "Product successfully deleted")
    @DeleteMapping("/{id}")
    @RequiresFeatureToggle("kittyProducts")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}