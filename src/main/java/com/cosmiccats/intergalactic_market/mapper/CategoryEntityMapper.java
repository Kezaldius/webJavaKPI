package com.cosmiccats.intergalactic_market.mapper;

import com.cosmiccats.intergalactic_market.domain.Category;
import com.cosmiccats.intergalactic_market.repository.entity.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapper {
    Category toDomain(CategoryEntity entity);
    CategoryEntity toEntity(Category domain);
}