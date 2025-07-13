package com.pinkcat.quickreservemvp.common.exceptions;

public enum ErrorMessageCode {
  SUCCESS(20000, "success"),
  ERROR(50000, "error"),

  // Auth: 40100~40199
  DUPLICATED_USER_ID(40101, "이미 존재하는 사용자 ID입니다."),
  LOGIN_FAILED(40102, "아이디 또는 비밀번호가 일치하지 않습니다."),

  // Customer: 40200~40299
  CUSTOMER_INACTIVE(40201, "비활성화된 계정입니다."),
  // ErrorMessageCode.java
  INVALID_PASSWORD(40202, "현재 비밀번호가 일치하지 않습니다."),

  // Product : 40300~40399
  NO_SUCH_PRODUCT(40301, "존재하지 않는 상품입니다."),
  NO_SUCH_CATEGORY(40302, "존재하지 않는 카테고리입니다."),
  NO_SUCH_CATEGORY_PRODUCT(40303, "카테고리가 존재하지 않는 상품입니다."),
  PRODUCT_UNAVAILABLE(40404, "현재 구매가 불가능한 상품입니다."),
  OUT_OF_STOCK(40405, "재고가 부족합니다."),


  // Order & OrderItem : 40400 ~ 40499
  NO_SUCH_ORDER(40401, "존재하지 않는 주문입니다"),
  NO_SUCH_ORDER_ITEM(40402, "존재하지 않는 주문 상품입니다."),

  // Review : 40500 ~ 40599
  NO_SUCH_REVIEW(40501, "존재하지 않는 리뷰입니다."),
  INVALID_USER(40502, "본인이 작성한 리뷰가 아닙니다."),

  // HotDeal : 40600 ~ 40699
  NO_SUCH_HOT_DEAL(40601, "존재하지 않는 핫딜입니다."),
  INVALID_HOT_DEAL(40601, "유효하지 않은 핫딜입니다."),

  // Cart : 40700 ~ 40799
  NO_SUCH_CART_ITEM(40701, "존재하지 않는 장바구니 상품입니다."),
  INVALID_QUANTITY(40702, "장바구니 상품의 최소 수량은 1개입니다."),

  // Payment : 40800 ~ 40899
  NO_SUCH_PAYMENT(40801, "결제건이 존재하지 않습니다.");

  private final int codeValue;
  private final String message;

  ErrorMessageCode(int codeValue, String message) {
    this.codeValue = codeValue;
    this.message = message;
  }

  public int getCode() {
    return codeValue;
  }

  public String printMessage() {
    return message;
  }
}
