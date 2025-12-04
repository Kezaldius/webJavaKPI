package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Category;
import com.cosmiccats.intergalactic_market.exceptions.PersistenceException;
import com.cosmiccats.intergalactic_market.mapper.CategoryEntityMapper;
import com.cosmiccats.intergalactic_market.repository.CategoryRepository;
import com.cosmiccats.intergalactic_market.repository.entity.CategoryEntity;
import com.cosmiccats.intergalactic_market.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImplementation implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryMapper;

    @Override
    @Transactional
    public Category createCategory(Category categoryDomain) {
        try {
            CategoryEntity entity = categoryMapper.toEntity(categoryDomain);
            entity = categoryRepository.save(entity);
            return categoryMapper.toDomain(entity);
        } catch (DataAccessException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        try {
            return categoryRepository.findAll().stream()
                    .map(categoryMapper::toDomain)
                    .toList();
        } catch (DataAccessException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        try {
            return categoryRepository.findById(id)
                    .map(categoryMapper::toDomain)
                    .orElseThrow(() -> new RuntimeException("Category with id " + id + " not found"));
        } catch (DataAccessException e) {
            throw new PersistenceException(e);
        }
    }
}