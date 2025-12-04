package com.cosmiccats.intergalactic_market.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Domain: Order Entity Tests")
class OrderTest {

    @Test
    void shouldAddItemToOrderCorrectly() {
        Order order = Order.builder()
                .id(1L)
                .customerEmail("meow@space.com")
                .createdAt(LocalDateTime.now())
                .build();

        Product product = Product.builder()
                .id(10L)
                .name("Space Laser")
                .price(50.0)
                .build();

        Order updatedOrder = order.addItem(product, 5);

        assertThat(updatedOrder.getItems()).hasSize(1);
        assertThat(updatedOrder.getItems().get(0).getProduct()).isEqualTo(product);
        assertThat(updatedOrder.getItems().get(0).getQuantity()).isEqualTo(5);

       assertThat(order.getItems()).isEmpty();
    }
}