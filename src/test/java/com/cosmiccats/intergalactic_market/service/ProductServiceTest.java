package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.exceptions.ProductNotFoundException;
import com.cosmiccats.intergalactic_market.mapper.ProductEntityMapper;
import com.cosmiccats.intergalactic_market.repository.ProductRepository;
import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductEntityMapper productMapper;

    @InjectMocks
    private ProductServiceImplementation productService;

    @Test
    @DisplayName("Should return all initial products")
    void getAllProducts_ShouldReturnInitialProducts() {
        List<ProductEntity> entityList = new ArrayList<>();
        ProductEntity entity1 = new ProductEntity();
        entity1.setId(1L);
        entityList.add(entity1);

        ProductEntity entity2 = new ProductEntity();
        entity2.setId(2L);
        entityList.add(entity2);

        Product domain1 = Product.builder().id(1L).name("Anti Gravity Yarn Balls").price(150.50).description("Desc").build();
        Product domain2 = Product.builder().id(2L).name("Milky Way Cosmic Milk").price(99.99).description("Desc").build();

        when(productRepository.findAll()).thenReturn(entityList);
        when(productMapper.toDomain(entity1)).thenReturn(domain1);
        when(productMapper.toDomain(entity2)).thenReturn(domain2);

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
    }

    @Test
    @DisplayName("Should return product by ID when it exists")
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        ProductEntity entity = new ProductEntity();
        entity.setId(1L);
        entity.setName("Anti Gravity Yarn Balls");

        Product domain = Product.builder().id(1L).name("Anti Gravity Yarn Balls").price(150.50).description("Desc").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(productMapper.toDomain(entity)).thenReturn(domain);

        Product product = productService.getProductById(1L);

        assertNotNull(product);
        assertEquals("Anti Gravity Yarn Balls", product.getName());
    }

    @Test
    @DisplayName("Should add a new product and assign an ID")
    void createProduct_ShouldAddNewProductAndAssignId() {
        Product newProduct = Product.builder().name("Laser Pointer").price(45.0).description("For cosmic cats").build();

        ProductEntity entityToSave = new ProductEntity();
        ProductEntity savedEntity = new ProductEntity();
        savedEntity.setId(3L);
        savedEntity.setName("Laser Pointer");

        Product savedDomain = Product.builder().id(3L).name("Laser Pointer").price(45.0).description("For cosmic cats").build();

        when(productMapper.toEntity(newProduct)).thenReturn(entityToSave);
        when(productRepository.save(entityToSave)).thenReturn(savedEntity);
        when(productMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        Product result = productService.createProduct(newProduct);

        assertEquals(3L, result.getId());
        assertEquals("Laser Pointer", result.getName());
    }

    @Test
    @DisplayName("Should update an existing product")
    void updateProduct_WhenProductExists_ShouldUpdateAndReturnProduct() {
        ProductEntity existingEntity = new ProductEntity();
        existingEntity.setId(1L);
        existingEntity.setName("Old Yarn");

        ProductEntity updatedEntity = new ProductEntity();
        updatedEntity.setId(1L);
        updatedEntity.setName("Super Yarn Balls");

        Product updatedDetails = Product.builder().name("Super Yarn Balls").price(200.0).description("Upgraded").build();
        Product resultDomain = Product.builder().id(1L).name("Super Yarn Balls").price(200.0).description("Upgraded").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(productRepository.save(existingEntity)).thenReturn(updatedEntity);
        when(productMapper.toDomain(updatedEntity)).thenReturn(resultDomain);

        Product updatedProduct = productService.updateProduct(1L, updatedDetails);

        assertNotNull(updatedProduct);
        assertEquals("Super Yarn Balls", updatedProduct.getName());
    }

    @Test
    @DisplayName("Should remove a product by ID")
    void deleteProduct_ShouldRemoveProductFromMap() {

        productService.deleteProduct(2L);

        verify(productRepository, times(1)).deleteById(2L);
    }

    @Test
    @DisplayName("Should throw exception when updating a non-existent product")
    void updateProduct_WhenProductDoesNotExist_ShouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Product someDetails = Product.builder().name("Doesn't matter").price(1.0).description("This will fail").build();

        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(99L, someDetails);
        });
    }

    @Test
    @DisplayName("Should return empty optional for non-existent product ID")
    void getProductById_WhenProductDoesNotExist_ShouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(99L));
    }
}