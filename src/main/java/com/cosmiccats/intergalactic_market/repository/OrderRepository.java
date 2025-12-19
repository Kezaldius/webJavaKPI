package com.cosmiccats.intergalactic_market.repository;

import com.cosmiccats.intergalactic_market.dto.TopProductDto;
import com.cosmiccats.intergalactic_market.repository.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT p.name as productName, SUM(oi.quantity) as totalSold " +
            "FROM OrderItemEntity oi JOIN oi.product p " +
            "GROUP BY p.name " +
            "ORDER BY totalSold DESC")
    List<TopProductDto> findTopSellingProducts();
}