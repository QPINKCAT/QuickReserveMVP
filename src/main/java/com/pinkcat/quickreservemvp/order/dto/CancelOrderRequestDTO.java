package com.pinkcat.quickreservemvp.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CancelOrderRequestDTO {
    List<Long> orderItemIds;
}