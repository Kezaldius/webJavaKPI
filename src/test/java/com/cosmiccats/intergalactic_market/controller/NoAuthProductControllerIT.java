package com.cosmiccats.intergalactic_market.controller;

import com.cosmiccats.intergalactic_market.AbstractIT;
import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.dto.ProductDTO;
import com.cosmiccats.intergalactic_market.dto.ProductRequest;
import com.cosmiccats.intergalactic_market.mapper.ProductMapper;
import com.cosmiccats.intergalactic_market.service.FeatureToggleService;
import com.cosmiccats.intergalactic_market.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("no-auth")
@AutoConfigureMockMvc
public class NoAuthProductControllerIT extends AbstractIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    @MockitoSpyBean
    private FeatureToggleService featureToggleService;

    @Test
    @DisplayName("GET /products - Should be accessible WITHOUT JWT in no-auth profile")
    void getAllProducts_ShouldBeAccessibleWithoutAuth() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /products - Should be accessible WITHOUT JWT, but Feature Toggles still apply")
    void createProduct_ShouldBeAccessibleWithoutAuth() throws Exception {
        when(featureToggleService.isEnabled("cosmoCats")).thenReturn(true);

        ProductRequest request = new ProductRequest("No-Auth galaxy Yarn", 50.0, "Test123");
        Product mockProduct = Product.builder().id(1L).name("No-Auth galaxy Yarn").build();
        ProductDTO mockDto = new ProductDTO(1L, "No-Auth galaxy Yarn", 50.0, "Test123");

        when(productMapper.toDomain(any())).thenReturn(mockProduct);
        when(productService.createProduct(any())).thenReturn(mockProduct);
        when(productMapper.toDto(any())).thenReturn(mockDto);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}