package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.AbstractIT;
import com.cosmiccats.intergalactic_market.domain.Order;
import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.repository.CategoryRepository;
import com.cosmiccats.intergalactic_market.repository.OrderRepository;
import com.cosmiccats.intergalactic_market.repository.ProductRepository;
import com.cosmiccats.intergalactic_market.repository.entity.CategoryEntity;
import com.cosmiccats.intergalactic_market.repository.entity.OrderEntity;
import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class OrderServiceIT extends AbstractIT {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldCreateOrderWithItems() {
        CategoryEntity category = new CategoryEntity();
        category.setName("Weapons");
        categoryRepository.save(category);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setName("Blaster");
        productEntity.setPrice(500.0);
        productEntity.setCategory(category);
        productEntity = productRepository.save(productEntity);

        Product domainProduct = Product.builder()
                .id(productEntity.getId())
                .name("Blaster")
                .price(500.0)
                .build();

        Order newOrder = Order.builder()
                .customerEmail("cap@cosmos.com")
                .createdAt(LocalDateTime.now())
                .build();

        newOrder = newOrder.addItem(domainProduct, 2);

        Order savedOrder = orderService.createOrder(newOrder);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getItems()).hasSize(1);

        Optional<OrderEntity> fromDb = orderRepository.findById(savedOrder.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getItems()).hasSize(1);
    }
}