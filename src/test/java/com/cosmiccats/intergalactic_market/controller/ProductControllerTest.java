package com.cosmiccats.intergalactic_market.controller;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.dto.ProductDTO;
import com.cosmiccats.intergalactic_market.dto.ProductRequest;
import com.cosmiccats.intergalactic_market.mapper.ProductMapper;
import com.cosmiccats.intergalactic_market.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getAllProducts_ShouldReturnListOfProducts() throws Exception {

        Product product1 = new Product(1L, "Starship", 100000.0, "Fast ship");
        ProductDTO dto1 = new ProductDTO(1L, "Starship", 100000.0, "Fast ship");
        List<Product> allProducts = Arrays.asList(product1);

        given(productService.getAllProducts()).willReturn(allProducts);
        given(productMapper.toDto(product1)).willReturn(dto1);

        mockMvc.perform(get("/api/v1/products")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Starship")));
    }

    @Test
    void getProductById_WhenProductFound_ShouldReturnProduct() throws Exception {

        Product product = new Product(1L, "Starship", 100000.0, "Fast ship");
        ProductDTO dto = new ProductDTO(1L, "Starship", 100000.0, "Fast ship");

        given(productService.getProductById(1L)).willReturn(Optional.of(product));
        given(productMapper.toDto(product)).willReturn(dto);

        mockMvc.perform(get("/api/v1/products/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Starship")));
    }

    @Test
    void getProductById_WhenProductNotFound_ShouldReturnNotFound() throws Exception {

        given(productService.getProductById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/99")).andExpect(status().isNotFound());
    }

    @Test
    void createProduct_WithValidRequest_ShouldReturnCreated() throws Exception {

        ProductRequest request = new ProductRequest("Cosmic star", 500.0, "A star");
        Product productToCreate = new Product(null, "Cosmic star", 500.0, "A star");
        Product createdProduct = new Product(1L, "Cosmic star", 500.0, "A star");
        ProductDTO responseDto = new ProductDTO(1L, "Cosmic star", 500.0, "A star");

        given(productMapper.toEntity(any(ProductRequest.class))).willReturn(productToCreate);
        given(productService.createProduct(productToCreate)).willReturn(createdProduct);
        given(productMapper.toDto(createdProduct)).willReturn(responseDto);


        mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Cosmic star")));
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnOk() throws Exception {

        ProductRequest request = new ProductRequest("Updated star", 550.0, "An updated star");
        Product productDetails = new Product(null, "Updated star", 550.0, "An updated star");
        Product updatedProduct = new Product(1L, "Updated star", 550.0, "An updated star");
        ProductDTO responseDto = new ProductDTO(1L, "Updated star", 550.0, "An updated star");

        given(productMapper.toEntity(any(ProductRequest.class))).willReturn(productDetails);
        given(productService.updateProduct(eq(1L), any(Product.class))).willReturn(updatedProduct);
        given(productMapper.toDto(updatedProduct)).willReturn(responseDto);

        mockMvc.perform(put("/api/v1/products/1").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(550.0)));
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {

        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/1")).andExpect(status().isNoContent());
    }
}
