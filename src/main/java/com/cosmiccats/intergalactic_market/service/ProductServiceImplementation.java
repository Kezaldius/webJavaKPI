package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.exceptions.PersistenceException;
import com.cosmiccats.intergalactic_market.exceptions.ProductNotFoundException;
import com.cosmiccats.intergalactic_market.mapper.ProductEntityMapper;
import com.cosmiccats.intergalactic_market.repository.ProductRepository;
import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final ProductEntityMapper productMapper;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDomain)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product createProduct(Product product) {
        try {
            ProductEntity entity = productMapper.toEntity(product);
            entity = productRepository.save(entity);
            return productMapper.toDomain(entity);
        }catch (DataAccessException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        try{
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        entity.setName(productDetails.getName());
        entity.setPrice(productDetails.getPrice());
        entity.setDescription(productDetails.getDescription());

        return productMapper.toDomain(productRepository.save(entity));
        }catch (DataAccessException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        }catch (DataAccessException e) {
            throw new PersistenceException(e);
        }
    }
}