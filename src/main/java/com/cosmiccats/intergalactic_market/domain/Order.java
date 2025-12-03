package com.cosmiccats.intergalactic_market.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private LocalDateTime createdAt;
    private String customerEmail;

    private List<OrderItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        this.items.add(new OrderItem(null, this, product, quantity));
    }
}