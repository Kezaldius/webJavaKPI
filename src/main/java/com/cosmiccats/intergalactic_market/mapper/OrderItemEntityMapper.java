package com.cosmiccats.intergalactic_market.mapper;

import com.cosmiccats.intergalactic_market.domain.OrderItem;
import com.cosmiccats.intergalactic_market.repository.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductEntityMapper.class})
public interface OrderItemEntityMapper {
    @Mapping(target = "product", source = "product")
    OrderItem toDomain(OrderItemEntity entity);

    @Mapping(target = "order", ignore = true)
    OrderItemEntity toEntity(OrderItem domain);
}