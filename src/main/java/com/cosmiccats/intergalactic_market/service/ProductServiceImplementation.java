package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.exceptions.ProductNotFoundException;
import com.cosmiccats.intergalactic_market.mapper.ProductEntityMapper;
import com.cosmiccats.intergalactic_market.repository.ProductRepository;
import com.cosmiccats.intergalactic_market.repository.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final ProductEntityMapper productMapper;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity savedEntity = productRepository.save(entity);
        return productMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDomain);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        ProductEntity existingEntity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existingEntity.setName(productDetails.getName());
        existingEntity.setPrice(productDetails.getPrice());
        existingEntity.setDescription(productDetails.getDescription());

        ProductEntity updatedEntity = productRepository.save(existingEntity);
        return productMapper.toDomain(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}