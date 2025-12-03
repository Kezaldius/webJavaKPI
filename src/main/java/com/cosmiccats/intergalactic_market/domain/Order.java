package com.cosmiccats.intergalactic_market.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class Order {
    Long id;
    LocalDateTime createdAt;
    String customerEmail;

    @Builder.Default
    List<OrderItem> items = new ArrayList<>();

    public Order addItem(Product product, int quantity) {
        OrderItem newItem = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .build();

        List<OrderItem> newItems = new ArrayList<>(this.items);
        newItems.add(newItem);

        return this.toBuilder()
                .items(newItems)
                .build();
    }
}