package com.cosmiccats.intergalactic_market.domain;

import lombok.Data;

@Data
public class CartItem {
    private Product product;
    private int quantity;
}
