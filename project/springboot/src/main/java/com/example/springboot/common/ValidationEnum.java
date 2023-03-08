package com.example.springboot.common;

public enum ValidationEnum {
    LOGIN(1), FORGET_PASSWORD(2),USER_REGIST(3);
    private Integer code;

    public Integer getCode() {
        return code;
    }

    ValidationEnum(Integer code) {
        this.code = code;
    }
}
