package com.cosmiccats.intergalactic_market.mapper;

import com.cosmiccats.intergalactic_market.domain.Product;
import com.cosmiccats.intergalactic_market.dto.ProductDTO;
import com.cosmiccats.intergalactic_market.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void shouldMapProductRequestToProductEntity() {
        ProductRequest requestDto = new ProductRequest();
        requestDto.setName("Cosmic Lightbulb");
        requestDto.setPrice(99.99);
        requestDto.setDescription("A very bright bulb.");

        Product product = productMapper.toEntity(requestDto);

        assertNotNull(product);
        assertEquals("Cosmic Lightbulb", product.getName());
        assertEquals(99.99, product.getPrice());
        assertEquals("A very bright bulb.", product.getDescription());
        assertNull(product.getId());
    }

    @Test
    void shouldMapProductEntityToProductDto() {
        Product product = new Product(5L, "Galaxy Widget", 123.45, "A universal widget");

        ProductDTO dto = productMapper.toDto(product);

        assertNotNull(dto);
        assertEquals(5L, dto.getId());
        assertEquals("Galaxy Widget", dto.getName());
        assertEquals(123.45, dto.getPrice());
        assertEquals("A universal widget", dto.getDescription());
    }
}