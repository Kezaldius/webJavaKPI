package com.cosmiccats.intergalactic_market.DTO;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import com.cosmiccats.intergalactic_market.validation.CosmicWordChecker;
import io.swagger.v3.oas.annotations.media.Schema; 

@Data
@Schema(description = "Дані для створення або оновлення товару")
public class ProductRequest {
    @NotEmpty(message = "Product name cannot be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")

    @CosmicWordChecker
    @Schema(description = "Назва товару. Повинна містити космічні терміни (star, galaxy, comet)", 
            example = "Зоряний пил 'Galaxy Dust'")
    private String name;

    @Schema(description = "Ціна товару. Повинна бути строго більше 0", example = "99.99")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;

    @Schema(description = "Детальний опис космічного товару", example = "Найкращий пил з туманності Андромеди")
    private String description;
}
