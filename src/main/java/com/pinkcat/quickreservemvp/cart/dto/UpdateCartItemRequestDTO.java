package com.pinkcat.quickreservemvp.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateCartItemRequestDTO {
    @NotNull
    private Long cartItemId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
