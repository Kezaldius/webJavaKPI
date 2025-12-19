package com.cosmiccats.intergalactic_market.repository;

import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}