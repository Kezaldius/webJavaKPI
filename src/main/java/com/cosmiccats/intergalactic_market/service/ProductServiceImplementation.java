package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.exceptions.CategoryNotFoundException;
import com.cosmiccats.intergalactic_market.exceptions.PersistenceException;
import com.cosmiccats.intergalactic_market.exceptions.ProductNotFoundException;
import com.cosmiccats.intergalactic_market.mapper.ProductEntityMapper;
import com.cosmiccats.intergalactic_market.repository.CategoryRepository;
import com.cosmiccats.intergalactic_market.repository.ProductRepository;
import com.cosmiccats.intergalactic_market.repository.entity.CategoryEntity;
import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductEntityMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDomain)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public Product createProduct(Product product) {
        try {
            ProductEntity entity = productMapper.toEntity(product);

            if (product.getCategory() != null && product.getCategory().getId() != null) {
                Long catId = product.getCategory().getId();
                CategoryEntity categoryEntity = categoryRepository.findById(catId)
                        .orElseThrow(() -> new CategoryNotFoundException(catId));
                entity.setCategory(categoryEntity);
            }

            entity = productRepository.save(entity);
            return productMapper.toDomain(entity);
        } catch (DataAccessException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public Product updateProduct(Long id, Product productDetails) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        try {
            entity.setName(productDetails.getName());
            entity.setPrice(productDetails.getPrice());
            entity.setDescription(productDetails.getDescription());

            if (productDetails.getCategory() != null && productDetails.getCategory().getId() != null) {
                Long catId = productDetails.getCategory().getId();
                CategoryEntity categoryEntity = categoryRepository.findById(catId)
                        .orElseThrow(() -> new CategoryNotFoundException(catId));
                entity.setCategory(categoryEntity);
            }

            return productMapper.toDomain(productRepository.save(entity));
        } catch (DataAccessException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new PersistenceException(e);
        }
    }
}