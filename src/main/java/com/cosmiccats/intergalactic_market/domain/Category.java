package com.cosmiccats.intergalactic_market.domain;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class Category {
    Long id;
    String name;

    @Builder.Default
    List<Product> products = new ArrayList<>();
}
