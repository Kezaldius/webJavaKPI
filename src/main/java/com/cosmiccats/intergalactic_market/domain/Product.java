package com.cosmiccats.intergalactic_market.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private double price;
    private String description;
    private Category category;

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "'}";
    }
}