package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.AbstractIT;
import com.cosmiccats.intergalactic_market.domain.Category;
import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.repository.CategoryRepository;
import com.cosmiccats.intergalactic_market.repository.ProductRepository;
import com.cosmiccats.intergalactic_market.repository.entity.CategoryEntity;
import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@WithMockUser(username = "test-cosmo-cat")
class ProductServiceIT extends AbstractIT {

    private static final String CATEGORY_NAME = "Cosmic Food";

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldCreateAndRetrieveProduct() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(CATEGORY_NAME);
        categoryEntity = categoryRepository.save(categoryEntity);

        Category domainCategory = Category.builder()
                .id(categoryEntity.getId())
                .name(CATEGORY_NAME)
                .build();

        Product newProduct = Product.builder()
                .name("Galaxy Chocolate Bar")
                .price(5.50)
                .description("Delicious")
                .category(domainCategory)
                .build();

        Product savedProduct = productService.createProduct(newProduct);

        assertThat(savedProduct.getId()).isNotNull();

        Optional<ProductEntity> retrieved = productRepository.findById(savedProduct.getId());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Galaxy Chocolate Bar");
    }

    @Test
    void shouldUpdateProduct() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(CATEGORY_NAME);
        categoryEntity = categoryRepository.save(categoryEntity);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("Stale Star Bread");
        productEntity.setPrice(1.0);
        productEntity.setCategory(categoryEntity);
        productEntity = productRepository.save(productEntity);

        Category domainCategory = Category.builder()
                .id(categoryEntity.getId())
                .name(CATEGORY_NAME)
                .build();

        Product updateDetails = Product.builder()
                .name("Fresh Comet Bread")
                .price(12.0)
                .description("Baked fresh")
                .category(domainCategory)
                .build();

        productService.updateProduct(productEntity.getId(), updateDetails);

        ProductEntity updated = productRepository.findById(productEntity.getId()).orElseThrow();
        assertThat(updated.getPrice()).isEqualTo(12.0);
        assertThat(updated.getName()).isEqualTo("Fresh Comet Bread");
    }

    @Test
    void shouldDeleteProduct() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(CATEGORY_NAME);
        categoryRepository.save(categoryEntity);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("Radioactive Star Soup");
        productEntity.setPrice(2.0);
        productEntity.setCategory(categoryEntity);
        productEntity = productRepository.save(productEntity);

        productService.deleteProduct(productEntity.getId());

        assertThat(productRepository.findById(productEntity.getId())).isEmpty();
    }

    @Test
    void shouldGetAllProducts() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(CATEGORY_NAME);
        categoryRepository.save(categoryEntity);

        ProductEntity p1 = new ProductEntity();
        p1.setName("Galaxy Cheese");
        p1.setPrice(10.0);
        p1.setCategory(categoryEntity);
        productRepository.save(p1);

        ProductEntity p2 = new ProductEntity();
        p2.setName("Spicy Comet Chips");
        p2.setPrice(5.0);
        p2.setCategory(categoryEntity);
        productRepository.save(p2);

        List<Product> products = productService.getAllProducts();

        assertThat(products).hasSize(2);
    }
}