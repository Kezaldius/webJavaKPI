package com.cosmiccats.intergalactic_market.repository;

import com.cosmiccats.intergalactic_market.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}