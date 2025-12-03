package com.cosmiccats.intergalactic_market.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Category {
    private final Long id;
    private final String name;
    private final List<Product> products;

    public Category(Long id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products != null ? products : new ArrayList<>();
    }
}
