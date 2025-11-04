package com.cosmiccats.intergalactic_market.controller;

import com.cosmiccats.intergalactic_market.dto.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
    }

    @Test
    void getAllProducts_ShouldReturnInitialProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].name", not(emptyOrNullString())));
    }

    @Test
    void createProduct_ShouldAddNewProduct() throws Exception {
        ProductRequest newProduct = new ProductRequest("Galaxy test", 777.77, "Ga;axy cloud in a jar");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Galaxy test")))
                .andExpect(jsonPath("$.price", is(777.77)));
    }

    @Test
    void getProductById_ShouldReturnExistingProduct() throws Exception {
        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", not(emptyOrNullString())));
    }

    @Test
    void updateProduct_ShouldModifyExistingProduct() throws Exception {
        ProductRequest updated = new ProductRequest("Updated Galaxy Yarn", 199.99, "Improved cosmic yarn");

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Galaxy Yarn")))
                .andExpect(jsonPath("$.price", is(199.99)));
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/products/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createProduct_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        ProductRequest invalid = new ProductRequest("", 100.0, "Invalid name");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_WithTooShortName_ShouldReturnBadRequest() throws Exception {
        ProductRequest invalid = new ProductRequest("ab", 50.0, "Too short name");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_WithoutCosmicWord_ShouldReturnBadRequest() throws Exception {
        ProductRequest invalid = new ProductRequest("Ordinary Item", 300.0, "No cosmic word");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_WithZeroPrice_ShouldReturnBadRequest() throws Exception {
        ProductRequest invalid = new ProductRequest("Galactic Comet", 0.0, "Invalid price");

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
