package com.nvlhnn.product.service.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Builder
public class CreateProductCommand {

    @NotNull
    private final String name;

    @NotNull
    private final BigDecimal price;
}
