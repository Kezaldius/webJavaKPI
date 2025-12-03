package com.cosmiccats.intergalactic_market.repository;

import com.cosmiccats.intergalactic_market.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}