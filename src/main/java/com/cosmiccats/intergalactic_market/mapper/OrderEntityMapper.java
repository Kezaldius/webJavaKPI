package com.cosmiccats.intergalactic_market.mapper;

import com.cosmiccats.intergalactic_market.domain.Order;
import com.cosmiccats.intergalactic_market.repository.entity.OrderEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {OrderItemEntityMapper.class})
public interface OrderEntityMapper {
    Order toDomain(OrderEntity entity);
    OrderEntity toEntity(Order domain);

    @AfterMapping
    default void linkItems(@MappingTarget OrderEntity entity) {
        if (entity.getItems() != null) {
            entity.getItems().forEach(item -> item.setOrder(entity));
        }
    }
}