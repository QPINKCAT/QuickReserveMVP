package com.pinkcat.quickreservemvp.common.model;

import lombok.Data;

@Data
public class BaseView {
    Long id;

    Long createdAt;
    Long updatedAt;
    Long deletedAt;
}