package com.pinkcat.quickreservemvp.hotdeal.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HotDealListResponseDTO {
    @NotNull
    int page;

    @NotNull
    int totalPages;

    @NotNull
    int size;

    @Builder.Default
    List<HotDeal> hotDealList = new ArrayList<>();

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class HotDeal{
        @NotNull
        private Long hotDealPk;

        @NotNull
        @Length(max = 255)
        private String name;

        @NotNull
        private String description;

        @NotNull
        private LocalDateTime startAt;

        @NotNull
        private LocalDateTime endAt;
    }
}
