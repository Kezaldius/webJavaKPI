package com.cosmiccats.intergalactic_market.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class OrderItem {
    Long id;
    Product product;
    int quantity;
}