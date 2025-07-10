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

  // Order : 40400 ~ 40499
  NO_SUCH_ORDER_ITEM(40401, "존재하지 않는 주문 상품입니다.");

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
