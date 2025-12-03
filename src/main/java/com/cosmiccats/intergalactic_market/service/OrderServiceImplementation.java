package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Order;
import com.cosmiccats.intergalactic_market.mapper.OrderEntityMapper;
import com.cosmiccats.intergalactic_market.repository.OrderRepository;
import com.cosmiccats.intergalactic_market.repository.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEntityMapper orderMapper;

    @Override
    public Order createOrder(Order order) {
        OrderEntity entity = orderMapper.toEntity(order);
        entity = orderRepository.save(entity);
        return orderMapper.toDomain(entity);
    }
}