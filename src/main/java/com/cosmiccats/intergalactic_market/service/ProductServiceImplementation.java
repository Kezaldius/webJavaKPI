package com.cosmiccats.intergalactic_market.service;

import com.cosmiccats.intergalactic_market.domain.Product;
import org.springframework.stereotype.Service;
import com.cosmiccats.intergalactic_market.exceptions.ResourceMissing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductServiceImplementation implements ProductService{
    private final ConcurrentHashMap<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public ProductServiceImplementation() {
        createProduct(new Product(null, "Антигравітаційні клубки ниток", 150.50, "Клубки, що ніколи не падають."));
        createProduct(new Product(null, "Космічне молоко 'Мілківей'", 99.99, "Молоко від космічних корів."));
    }

    @Override
    public Product createProduct(Product product) {
        long newId = idCounter.incrementAndGet(); 
        product.setId(newId);
        products.put(newId, product);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        if (!products.containsKey(id)) {
            throw new ResourceMissing("Product not found with id: " + id);
        }
        productDetails.setId(id); 
        products.put(id, productDetails);
        return productDetails;
    }

    @Override
    public void deleteProduct(Long id) {
    if (products.remove(id) == null) { 
        throw new ResourceMissing("Product not found with id: " + id);
        }
    }
}
