package com.cosmiccats.intergalactic_market.controller;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.dto.ProductRequest;
import com.cosmiccats.intergalactic_market.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@DisplayName("Product Controller Integration Tests")
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private Product product;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Galaxy Yarn", 199.99, "Improved cosmic yarn");
        productRequest = new ProductRequest("Galaxy Yarn", 199.99, "Improved cosmic yarn");
        reset(productService);
    }

    @Test
    @DisplayName("GET /products - Should return list of products")
    void getAllProducts_ShouldReturnProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(product));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Galaxy Yarn")));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    @DisplayName("POST /products - Should create a new product")
    void createProduct_ShouldAddNewProduct() throws Exception {
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Galaxy Yarn")));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    @DisplayName("GET /products/{id} - Should return existing product")
    void getProductById_ShouldReturnExistingProduct() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Galaxy Yarn")));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("GET /products/{id} - Should return 404 Not Found")
    void getProductById_WhenNotFound_ShouldReturn404() throws Exception {
        when(productService.getProductById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/99"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(99L);
    }

    @Test
    @DisplayName("PUT /products/{id} - Should modify existing product")
    void updateProduct_ShouldModifyExistingProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Galaxy Yarn")));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    @DisplayName("PUT /products/{id} - Should return 404 Not Found")
    void updateProduct_WhenNotFound_ShouldReturn404() throws Exception {
        when(productService.updateProduct(eq(99L), any(Product.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/products/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).updateProduct(eq(99L), any(Product.class));
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
        ProductRequest invalidRequest = new ProductRequest("Item", 100.0, "No cosmic word");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).createProduct(any(Product.class));
    }
}