package com.cosmiccats.intergalactic_market.mapper;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CategoryEntityMapper.class})
public interface ProductEntityMapper {
    Product toDomain(ProductEntity entity);
    ProductEntity toEntity(Product domain);
}