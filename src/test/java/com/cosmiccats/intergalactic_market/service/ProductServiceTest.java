package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProductServiceImplementation.class)
@DisplayName("Product Service Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceTest {

    @Autowired
    private ProductServiceImplementation productService;

    @Test
    @Order(1)
    @DisplayName("Should return all initial products")
    void getAllProducts_ShouldReturnInitialProducts() {
        List<Product> products = productService.getAllProducts();
        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    @Order(2)
    @DisplayName("Should return product by ID when it exists")
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        Optional<Product> productOptional = productService.getProductById(1L);
        assertTrue(productOptional.isPresent());
        assertEquals("Anti Gravity Yarn Balls", productOptional.get().getName());
    }

    @Test
    @Order(3)
    @DisplayName("Should add a new product and assign an ID")
    void createProduct_ShouldAddNewProductAndAssignId() {
        Product newProduct = new Product(null, "Laser Pointer", 45.0, "For cosmic cats");
        productService.createProduct(newProduct);
        assertEquals(3, productService.getAllProducts().size());
    }

    @Test
    @Order(4)
    @DisplayName("Should update an existing product")
    void updateProduct_WhenProductExists_ShouldUpdateAndReturnProduct() {
        Product updatedDetails = new Product(null, "Super Yarn Balls", 200.0, "Upgraded");
        Product updatedProduct = productService.updateProduct(1L, updatedDetails);
        assertNotNull(updatedProduct);
        assertEquals("Super Yarn Balls", updatedProduct.getName());
    }

    @Test
    @Order(5)
    @DisplayName("Should remove a product by ID")
    void deleteProduct_ShouldRemoveProductFromMap() {
        productService.deleteProduct(2L);
        assertTrue(productService.getProductById(2L).isEmpty());
        assertEquals(2, productService.getAllProducts().size());
    }

    @Test
    @DisplayName("Should throw exception when updating a non-existent product")
    void updateProduct_WhenProductDoesNotExist_ShouldThrowException() {
        Product someDetails = new Product(null, "Doesn't matter", 1.0, "This will fail");
        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(99L, someDetails);
        });
    }

    @Test
    @DisplayName("Should return empty optional for non-existent product ID")
    void getProductById_WhenProductDoesNotExist_ShouldReturnEmptyOptional() {
        Optional<Product> productOptional = productService.getProductById(99L);
        assertTrue(productOptional.isEmpty());
    }
}