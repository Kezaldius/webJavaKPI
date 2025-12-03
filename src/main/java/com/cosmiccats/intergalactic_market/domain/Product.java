package com.cosmiccats.intergalactic_market.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Product {
    Long id;
    String name;
    Double price;
    String description;
    Category category;
}