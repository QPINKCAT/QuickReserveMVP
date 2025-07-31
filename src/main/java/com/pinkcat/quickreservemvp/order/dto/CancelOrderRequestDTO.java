package com.pinkcat.quickreservemvp.order.dto;

import java.util.ArrayList;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CancelOrderRequestDTO {
    @Builder.Default
    List<Long> orderItemIds = new ArrayList<>();
}