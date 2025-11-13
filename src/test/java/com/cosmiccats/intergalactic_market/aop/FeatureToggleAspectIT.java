package com.cosmiccats.intergalactic_market.aop;

import com.cosmiccats.intergalactic_market.dto.ProductRequest;
import com.cosmiccats.intergalactic_market.dto.ProductDTO;
import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.mapper.ProductMapper;
import com.cosmiccats.intergalactic_market.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// Сподіваюсь правильно зрозумів що потрібен саме інтеграційний тест для рівня контролеру, а не сервісу
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "feature.toggles.cosmoCats=true",
        "feature.toggles.kittyProducts=false"
})
@DisplayName("Feature Toggle Aspect Integration Tests")
public class FeatureToggleAspectIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    @Test
    @DisplayName("When 'cosmoCats' feature is ENABLED, should allow POST and return 201 Created")
    void whenCosmoCatsFeatureIsEnabled_shouldAllowCreateProduct() throws Exception {
        ProductRequest request = new ProductRequest("Cosmic Star Dust", 10.0, "Shiny dust from distant galaxies");

        Product mockEntity = new Product();
        mockEntity.setId(1L);
        mockEntity.setName("Cosmic Star Dust");

        Product mockCreatedProduct = new Product();
        mockCreatedProduct.setId(1L);
        mockCreatedProduct.setName("Cosmic Star Dust");

        ProductDTO mockDTO = new ProductDTO(1L, "Cosmic Star Dust", 10.0, "Shiny dust from distant galaxies");

        when(productMapper.toEntity(any(ProductRequest.class))).thenReturn(mockEntity);
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
        ProductRequest request = new ProductRequest("Galaxy Explorer", 20.0, "Navigate through distant galaxies");

        Product mockEntity = new Product();
        mockEntity.setId(1L);
        mockEntity.setName("Galaxy Explorer");

        Product mockUpdatedProduct = new Product();
        mockUpdatedProduct.setId(1L);
        mockUpdatedProduct.setName("Galaxy Explorer");

        ProductDTO mockDTO = new ProductDTO(1L, "Galaxy Explorer", 20.0, "Navigate through distant galaxies");

        when(productMapper.toEntity(any(ProductRequest.class))).thenReturn(mockEntity);
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
