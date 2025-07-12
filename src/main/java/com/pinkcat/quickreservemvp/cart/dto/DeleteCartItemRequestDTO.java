package com.pinkcat.quickreservemvp.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class DeleteCartItemRequestDTO {
    @NotNull
    Long cartItemId;
}
