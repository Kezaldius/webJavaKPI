package com.cosmiccats.intergalactic_market.mapper;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CategoryEntityMapper.class})
public interface ProductEntityMapper {

    @Mapping(target = "category", qualifiedByName = "toDomainSummary")
    Product toDomain(ProductEntity entity);

    ProductEntity toEntity(Product domain);
}