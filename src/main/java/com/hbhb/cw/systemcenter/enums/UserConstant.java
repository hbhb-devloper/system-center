package com.hbhb.cw.systemcenter.enums;

import lombok.Getter;

@Getter
public enum UserConstant {
    /**
     * 用户默认密码
     */
    DEFAULT_PASSWORD("123456"),

    ;

    private final String value;

    UserConstant(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
