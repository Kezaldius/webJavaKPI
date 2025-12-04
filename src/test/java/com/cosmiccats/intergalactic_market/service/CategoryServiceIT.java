package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.AbstractIT;
import com.cosmiccats.intergalactic_market.domain.Category;
import com.cosmiccats.intergalactic_market.exceptions.CategoryNotFoundException;
import com.cosmiccats.intergalactic_market.repository.CategoryRepository;
import com.cosmiccats.intergalactic_market.repository.entity.CategoryEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryServiceIT extends AbstractIT {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldCreateCategory() {
        Category categoryDomain = Category.builder()
                .name("Galactic Drinks")
                .build();

        Category savedCategory = categoryService.createCategory(categoryDomain);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Galactic Drinks");

        List<CategoryEntity> entities = categoryRepository.findAll();
        assertThat(entities).hasSize(1);
        assertThat(entities.get(0).getName()).isEqualTo("Galactic Drinks");
    }

    @Test
    void shouldGetCategoryById() {
        CategoryEntity entity = new CategoryEntity();
        entity.setName("Star Snacks");
        entity = categoryRepository.save(entity);

        Category foundCategory = categoryService.getCategoryById(entity.getId());

        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getId()).isEqualTo(entity.getId());
        assertThat(foundCategory.getName()).isEqualTo("Star Snacks");
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(9999L));
    }

    @Test
    void shouldGetAllCategories() {
        CategoryEntity entity1 = new CategoryEntity();
        entity1.setName("Comet Desserts");
        categoryRepository.save(entity1);

        CategoryEntity entity2 = new CategoryEntity();
        entity2.setName("Nebula Main Dishes");
        categoryRepository.save(entity2);

        List<Category> categories = categoryService.getAllCategories();

        assertThat(categories).hasSize(2);
    }
}