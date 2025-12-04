package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.AbstractIT;
import com.cosmiccats.intergalactic_market.domain.Order;
import com.cosmiccats.intergalactic_market.domain.OrderItem;
import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.repository.CategoryRepository;
import com.cosmiccats.intergalactic_market.repository.OrderRepository;
import com.cosmiccats.intergalactic_market.repository.ProductRepository;
import com.cosmiccats.intergalactic_market.repository.entity.CategoryEntity;
import com.cosmiccats.intergalactic_market.repository.entity.OrderEntity;
import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    void shouldCreateOrder() {
        CategoryEntity cat = new CategoryEntity();
        cat.setName("Cosmic Drinks");
        cat = categoryRepository.save(cat);

        ProductEntity prodEntity = new ProductEntity();
        prodEntity.setName("Galaxy Cola");
        prodEntity.setPrice(10.0);
        prodEntity.setCategory(cat);
        prodEntity = productRepository.save(prodEntity);

        Product productDomain = Product.builder()
                .id(prodEntity.getId())
                .name("Galaxy Cola")
                .price(10.0)
                .build();

        OrderItem item = OrderItem.builder()
                .product(productDomain)
                .quantity(5)
                .build();

        Order order = Order.builder()
                .customerEmail("alien@space.com")
                .createdAt(LocalDateTime.now())
                .items(List.of(item))
                .build();

        Order savedOrder = orderService.createOrder(order);

        assertThat(savedOrder.getId()).isNotNull();

        List<OrderEntity> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);

        OrderEntity dbOrder = orders.get(0);
        assertThat(dbOrder.getCustomerEmail()).isEqualTo("alien@space.com");

        assertThat(dbOrder.getItems()).hasSize(1);
        assertThat(dbOrder.getItems().get(0).getQuantity()).isEqualTo(5);
        assertThat(dbOrder.getItems().get(0).getProduct().getName()).isEqualTo("Galaxy Cola");
    }
}