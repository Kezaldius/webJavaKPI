package com.cosmiccats.intergalactic_market.domain;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class Cart {
    private Long id;
    private List<CartItem> items = new ArrayList<>();
}
