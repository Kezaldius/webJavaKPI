package com.cosmiccats.intergalactic_market.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import com.cosmiccats.intergalactic_market.validation.CosmicWordChecker;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Data for creating or updating a product")
public class ProductRequest {
    @NotEmpty(message = "Product name cannot be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")

    @CosmicWordChecker
    @Schema(description = "Name of the product. Must contain cosmic terms (star, galaxy, comet)",
            example = "Galaxy Dust stardust")
    private String name;

    @Schema(description = "Price of the product. Must be strictly greater than 0",
            example = "99.99")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;

    @Schema(description = "Detailed description of the cosmic product",
            example = "The finest dust from the Andromeda nebula")
    private String description;
}
