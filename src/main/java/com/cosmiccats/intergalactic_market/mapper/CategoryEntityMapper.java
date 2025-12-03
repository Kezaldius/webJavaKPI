package com.cosmiccats.intergalactic_market.mapper;

import com.cosmiccats.intergalactic_market.domain.Category;
import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.repository.entity.CategoryEntity;
import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CategoryEntityMapper {

    @Autowired
    @Lazy
    protected ProductEntityMapper productEntityMapper;

    @Mapping(target = "products", expression = "java(mapProducts(entity.getProducts()))")
    public abstract Category toDomain(CategoryEntity entity);

    public abstract CategoryEntity toEntity(Category domain);

    @Named("toDomainSummary")
    @Mapping(target = "products", ignore = true)
    public abstract Category toDomainSummary(CategoryEntity entity);

    protected List<Product> mapProducts(List<ProductEntity> products) {
        if (products == null) {
            return new ArrayList<>();
        }
        return products.stream()
                .map(productEntityMapper::toDomain)
                .collect(Collectors.toList());
    }
}