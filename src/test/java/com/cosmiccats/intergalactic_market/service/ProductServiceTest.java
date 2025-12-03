package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.exceptions.ProductNotFoundException;
import com.cosmiccats.intergalactic_market.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImplementation productService;

    @Test
    @DisplayName("Should return all initial products")
    void getAllProducts_ShouldReturnInitialProducts() {
        List<Product> mockList = new ArrayList<>();
        mockList.add(new Product(1L, "Anti Gravity Yarn Balls", 150.50, "Desc"));
        mockList.add(new Product(2L, "Milky Way Cosmic Milk", 99.99, "Desc"));

        when(productRepository.findAll()).thenReturn(mockList);

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    @DisplayName("Should return product by ID when it exists")
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        Product product = new Product(1L, "Anti Gravity Yarn Balls", 150.50, "Desc");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> productOptional = productService.getProductById(1L);

        assertTrue(productOptional.isPresent());
        assertEquals("Anti Gravity Yarn Balls", productOptional.get().getName());
    }

    @Test
    @DisplayName("Should add a new product and assign an ID")
    void createProduct_ShouldAddNewProductAndAssignId() {
        Product newProduct = new Product(null, "Laser Pointer", 45.0, "For cosmic cats");
        Product savedProduct = new Product(3L, "Laser Pointer", 45.0, "For cosmic cats");

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct(newProduct);

        assertEquals(3L, result.getId());
        assertEquals("Laser Pointer", result.getName());
    }

    @Test
    @DisplayName("Should update an existing product")
    void updateProduct_WhenProductExists_ShouldUpdateAndReturnProduct() {
        Product existingProduct = new Product(1L, "Old Yarn", 100.0, "Old");
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));

        Product updatedDetails = new Product(null, "Super Yarn Balls", 200.0, "Upgraded");
        Product savedProduct = new Product(1L, "Super Yarn Balls", 200.0, "Upgraded");

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product updatedProduct = productService.updateProduct(1L, updatedDetails);

        assertNotNull(updatedProduct);
        assertEquals("Super Yarn Balls", updatedProduct.getName());
    }

    @Test
    @DisplayName("Should remove a product by ID")
    void deleteProduct_ShouldRemoveProductFromMap() {
        when(productRepository.existsById(2L)).thenReturn(true);

        productService.deleteProduct(2L);

        verify(productRepository, times(1)).deleteById(2L);
    }

    @Test
    @DisplayName("Should throw exception when updating a non-existent product")
    void updateProduct_WhenProductDoesNotExist_ShouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Product someDetails = new Product(null, "Doesn't matter", 1.0, "This will fail");

        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(99L, someDetails);
        });
    }

    @Test
    @DisplayName("Should return empty optional for non-existent product ID")
    void getProductById_WhenProductDoesNotExist_ShouldReturnEmptyOptional() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> productOptional = productService.getProductById(99L);
        assertTrue(productOptional.isEmpty());
    }
}