package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Order;

public interface OrderService {
    Order createOrder(Order order);
}