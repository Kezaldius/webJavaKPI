package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.AbstractIT;
import com.cosmiccats.intergalactic_market.domain.Category;
import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.repository.CategoryRepository;
import com.cosmiccats.intergalactic_market.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
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
        Category category = categoryRepository.save(new Category(CATEGORY_NAME));

        Product newProduct = new Product(null, "Galaxy Chocolate Bar", 5.50, "Delicious dark matter chocolate");
        newProduct.setCategory(category);

        Product savedProduct = productService.createProduct(newProduct);

        assertThat(savedProduct.getId()).isNotNull();

        Optional<Product> retrieved = productRepository.findById(savedProduct.getId());
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getName()).isEqualTo("Galaxy Chocolate Bar");
        assertThat(retrieved.get().getCategory().getName()).isEqualTo(CATEGORY_NAME);
    }

    @Test
    void shouldUpdateProduct() {
        Category category = categoryRepository.save(new Category(CATEGORY_NAME));

        Product product = new Product(null, "Stale Star Bread", 1.0, "Very hard bread");
        product.setCategory(category);
        productService.createProduct(product);

        Product updateDetails = new Product(null, "Fresh Comet Bread", 12.0, "Baked on a passing comet");

        productService.updateProduct(product.getId(), updateDetails);

        Product updated = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updated.getPrice()).isEqualTo(12.0);
        assertThat(updated.getName()).isEqualTo("Fresh Comet Bread");
    }

    @Test
    void shouldDeleteProduct() {
        Category category = categoryRepository.save(new Category(CATEGORY_NAME));

        Product product = new Product(null, "Radioactive Star Soup", 2.0, "Glowing green soup");
        product.setCategory(category);
        productService.createProduct(product);

        productService.deleteProduct(product.getId());

        assertThat(productRepository.findById(product.getId())).isEmpty();
    }

    @Test
    void shouldGetAllProducts() {
        Category category = categoryRepository.save(new Category(CATEGORY_NAME));

        Product p1 = new Product(null, "Galaxy Cheese", 10.0, "Made from Milky Way milk");
        p1.setCategory(category);
        productService.createProduct(p1);

        Product p2 = new Product(null, "Spicy Comet Chips", 5.0, "Hot as the sun");
        p2.setCategory(category);
        productService.createProduct(p2);

        List<Product> products = productService.getAllProducts();

        assertThat(products).hasSize(2);
    }
}