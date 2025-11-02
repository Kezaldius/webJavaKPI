package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplementationTest {

    private ProductServiceImplementation productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImplementation();
    }

    @Test
    void getAllProducts_ShouldReturnInitialProducts() {
        List<Product> products = productService.getAllProducts();
        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        Optional<Product> productOptional = productService.getProductById(1L);
        assertTrue(productOptional.isPresent());
        assertEquals("Anti Gravity Yarn Balls", productOptional.get().getName());
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnEmptyOptional() {
        Optional<Product> productOptional = productService.getProductById(99L);
        assertTrue(productOptional.isEmpty());
    }

    @Test
    void createProduct_ShouldAddNewProductAndAssignId() {
        Product newProduct = new Product(null, "Laser Pointer", 45.0, "For cosmic cats");
        Product createdProduct = productService.createProduct(newProduct);
        assertNotNull(createdProduct);
        assertEquals(3L, createdProduct.getId());
        assertEquals("Laser Pointer", createdProduct.getName());
        assertEquals(3, productService.getAllProducts().size());
    }

    @Test
    void updateProduct_WhenProductExists_ShouldUpdateAndReturnProduct() {
        Product updatedDetails = new Product(null, "Super Yarn Balls", 200.0, "Upgraded");
        Product updatedProduct = productService.updateProduct(1L, updatedDetails);
        assertNotNull(updatedProduct);
        assertEquals(1L, updatedProduct.getId());
        assertEquals("Super Yarn Balls", updatedProduct.getName());
        Optional<Product> productFromService = productService.getProductById(1L);
        assertTrue(productFromService.isPresent());
        assertEquals(200.0, productFromService.get().getPrice());
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldThrowException() {
        Product someDetails = new Product(null, "Doesn't matter", 1.0, "This will fail");
        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(99L, someDetails);
        });
    }

    @Test
    void deleteProduct_ShouldRemoveProductFromMap() {
        assertTrue(productService.getProductById(2L).isPresent());
        assertEquals(2, productService.getAllProducts().size());
        productService.deleteProduct(2L);
        assertTrue(productService.getProductById(2L).isEmpty());
        assertEquals(1, productService.getAllProducts().size());
    }
}
