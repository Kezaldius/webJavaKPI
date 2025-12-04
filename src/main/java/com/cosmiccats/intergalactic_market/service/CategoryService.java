package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Category;
import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
}