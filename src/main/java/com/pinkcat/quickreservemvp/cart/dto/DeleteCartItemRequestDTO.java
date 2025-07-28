package com.pinkcat.quickreservemvp.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeleteCartItemRequestDTO {
    @NotNull
    Long cartItemId;
}
