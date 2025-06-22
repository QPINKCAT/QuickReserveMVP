package com.pinkcat.quickreservemvp.wish.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteWishlistRequestDTO {

    @NotNull
    private Long wishId;
}
