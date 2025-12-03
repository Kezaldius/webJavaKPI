package com.cosmiccats.intergalactic_market.aop;

import com.cosmiccats.intergalactic_market.AbstractIT;
import com.cosmiccats.intergalactic_market.dto.ProductRequest;
import com.cosmiccats.intergalactic_market.dto.ProductDTO;
import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.mapper.ProductMapper;
import com.cosmiccats.intergalactic_market.service.FeatureToggleService;
import com.cosmiccats.intergalactic_market.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("Feature Toggle Aspect Integration Tests")
public class FeatureToggleAspectIT extends AbstractIT {

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
    @DisplayName("When 'cosmoCats' feature is ENABLED, should allow POST and return 201 Created")
    void whenCosmoCatsFeatureIsEnabled_shouldAllowCreateProduct() throws Exception {
        when(featureToggleService.isEnabled("cosmoCats")).thenReturn(true);

        ProductRequest request = new ProductRequest("Cosmic Star Dust", 10.0, "Shiny dust from distant galaxies");

        Product mockDomain = Product.builder().id(1L).name("Cosmic Star Dust").price(10.0).build();
        Product mockCreatedProduct = Product.builder().id(1L).name("Cosmic Star Dust").price(10.0).build();
        ProductDTO mockDTO = new ProductDTO(1L, "Cosmic Star Dust", 10.0, "Shiny dust from distant galaxies");

        when(productMapper.toDomain(any(ProductRequest.class))).thenReturn(mockDomain);
        when(productService.createProduct(any(Product.class))).thenReturn(mockCreatedProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(mockDTO);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(productService).createProduct(any(Product.class));
    }

    @Test
    @DisplayName("When 'cosmoCats' feature is ENABLED, should allow PUT and return 200 OK")
    void whenCosmoCatsFeatureIsEnabled_shouldAllowUpdateProduct() throws Exception {
        when(featureToggleService.isEnabled("cosmoCats")).thenReturn(true);
        ProductRequest request = new ProductRequest("Galaxy Explorer", 20.0, "Navigate through distant galaxies");

        Product mockDomain = Product.builder().id(1L).name("Galaxy Explorer").price(20.0).build();
        Product mockUpdatedProduct = Product.builder().id(1L).name("Galaxy Explorer").price(20.0).build();
        ProductDTO mockDTO = new ProductDTO(1L, "Galaxy Explorer", 20.0, "Navigate through distant galaxies");

        when(productMapper.toDomain(any(ProductRequest.class))).thenReturn(mockDomain);
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(mockUpdatedProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(mockDTO);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(productService).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    @DisplayName("When 'kittyProducts' feature is DISABLED, should block DELETE and return 403 Forbidden")
    void whenKittyProductsFeatureIsDisabled_shouldBlockDeleteProduct() throws Exception {
        when(featureToggleService.isEnabled("kittyProducts")).thenReturn(false);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isForbidden());

        verify(productService, never()).deleteProduct(any());
    }

    @Test
    @DisplayName("When endpoint has no @RequiresFeatureToggle, should always allow access")
    void whenEndpointIsNotProtected_shouldAlwaysAllowAccess() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk());

        verify(productService).getAllProducts();
    }
}