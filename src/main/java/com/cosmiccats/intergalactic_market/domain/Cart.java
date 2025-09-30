package com.cosmiccats.intergalactic_market.domain;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
public class Cart {
    private Long id;
    // Тут буде(напевно) зв'язок з користувачем    
    private List<CartItem> items = new ArrayList<>();
}
