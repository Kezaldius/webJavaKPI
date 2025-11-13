package com.cosmiccats.intergalactic_market.controller;

import com.cosmiccats.intergalactic_market.config.MappersTestConfiguration;
import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.dto.ProductRequest;
import com.cosmiccats.intergalactic_market.exceptions.GlobalExceptionHandler;
import com.cosmiccats.intergalactic_market.exceptions.ProductNotFoundException;
import com.cosmiccats.intergalactic_market.mapper.ProductMapper;
import com.cosmiccats.intergalactic_market.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIT {

    private static final Long PRODUCT_ID = 1L;
    private static final Long NON_EXISTENT_ID = 99L;
    private static final String PRODUCT_NAME = "Galaxy Yarn";
    private static final double PRODUCT_PRICE = 199.99;
    private static final String PRODUCT_DESCRIPTION = "Improved cosmic yarn";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductMapper productMapper;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("GET /products - Should return list of products")
    void getAllProducts_ShouldReturnProducts() throws Exception {
        Product product = new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_DESCRIPTION);
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(product));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(PRODUCT_NAME)));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("POST /products - Should create a new product")
    void createProduct_ShouldAddNewProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest(PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_DESCRIPTION);
        Product createdProduct = new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_DESCRIPTION);

        when(productService.createProduct(any(Product.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(PRODUCT_ID.intValue())))
                .andExpect(jsonPath("$.name", is(PRODUCT_NAME)));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    @DisplayName("GET /products/{id} - Should return existing product")
    void getProductById_ShouldReturnExistingProduct() throws Exception {
        Product product = new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_DESCRIPTION);
        when(productService.getProductById(PRODUCT_ID)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/products/" + PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(PRODUCT_ID.intValue())))
                .andExpect(jsonPath("$.name", is(PRODUCT_NAME)));

        verify(productService, times(1)).getProductById(PRODUCT_ID);
    }

    @Test
    @DisplayName("GET /products/{id} - Should return 404 Not Found")
    void getProductById_WhenNotFound_ShouldReturn404() throws Exception {
        when(productService.getProductById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/" + NON_EXISTENT_ID))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(NON_EXISTENT_ID);
    }

    @Test
    @DisplayName("PUT /products/{id} - Should modify existing product")
    void updateProduct_ShouldModifyExistingProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest(PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_DESCRIPTION);
        Product updatedProduct = new Product(PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_DESCRIPTION);

        when(productService.updateProduct(eq(PRODUCT_ID), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/v1/products/" + PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PRODUCT_NAME)));

        verify(productService, times(1)).updateProduct(eq(PRODUCT_ID), any(Product.class));
    }

    @Test
    @DisplayName("PUT /products/{id} - Should return 404 Not Found")
    void updateProduct_WhenNotFound_ShouldReturn404() throws Exception {
        ProductRequest productRequest = new ProductRequest(PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_DESCRIPTION);

        when(productService.updateProduct(eq(NON_EXISTENT_ID), any(Product.class)))
                .thenThrow(new ProductNotFoundException(NON_EXISTENT_ID));

        mockMvc.perform(put("/api/v1/products/" + NON_EXISTENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).updateProduct(eq(NON_EXISTENT_ID), any(Product.class));
    }

    @Test
    @DisplayName("DELETE /products/{id} - Should return No Content")
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        doNothing().when(productService).deleteProduct(2L);

        mockMvc.perform(delete("/api/v1/products/2"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(2L);
    }

    @Test
    @DisplayName("POST /products - With invalid data should return Bad Request")
    void createProduct_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        ProductRequest invalidRequest = new ProductRequest("", -10.0, "Invalid");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).createProduct(any(Product.class));
    }
}