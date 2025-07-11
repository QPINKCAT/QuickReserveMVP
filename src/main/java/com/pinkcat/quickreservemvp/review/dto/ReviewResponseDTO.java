package com.pinkcat.quickreservemvp.review.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ReviewResponseDTO {
    @NotNull
    private Integer rating;

    private String comment;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
