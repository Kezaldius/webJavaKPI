package com.cosmiccats.intergalactic_market.mapper;

import com.cosmiccats.intergalactic_market.DTO.ProductRequest;
import com.cosmiccats.intergalactic_market.DTO.ProductDTO;
import com.cosmiccats.intergalactic_market.domain.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product product);
    Product toEntity(ProductRequest requestDTO);
}
