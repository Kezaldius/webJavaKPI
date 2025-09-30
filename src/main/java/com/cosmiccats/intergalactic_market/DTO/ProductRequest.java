package com.cosmiccats.intergalactic_market.DTO;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import com.cosmiccats.intergalactic_market.validation.CosmicWordChecker; 

@Data
public class ProductRequest {
    @NotEmpty(message = "Product name cannot be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    
    @CosmicWordChecker
    private String name;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;

    private String description;
}
