package com.cosmiccats.intergalactic_market.mapper;

import com.cosmiccats.intergalactic_market.domain.Order;
import com.cosmiccats.intergalactic_market.domain.OrderItem;
import com.cosmiccats.intergalactic_market.repository.entity.OrderEntity;
import com.cosmiccats.intergalactic_market.repository.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductEntityMapper.class})
public interface OrderEntityMapper {

    Order toDomain(OrderEntity entity);

    OrderEntity toEntity(Order domain);

    @Mapping(target = "order", ignore = true)
    OrderItem toDomainItem(OrderItemEntity entity);

    @Mapping(target = "order", ignore = true)
    OrderItemEntity toEntityItem(OrderItem domain);
}