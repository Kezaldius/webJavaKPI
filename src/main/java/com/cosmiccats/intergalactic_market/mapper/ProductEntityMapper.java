package com.cosmiccats.intergalactic_market.mapper;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {
    @Mapping(target = "category", ignore = true)
    Product toDomain(ProductEntity entity);

    @Mapping(target = "category", ignore = true)
    ProductEntity toEntity(Product domain);
}