package com.cosmiccats.intergalactic_market.mapper;

import com.cosmiccats.intergalactic_market.domain.Category;
import com.cosmiccats.intergalactic_market.repository.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductEntityMapper.class})
public interface CategoryEntityMapper {
    Category toDomain(CategoryEntity entity);
    CategoryEntity toEntity(Category domain);
}