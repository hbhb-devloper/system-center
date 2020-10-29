package com.hbhb.cw.systemcenter.enums;

import lombok.Getter;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Getter
public enum UserErrorCode {

    AUTHOR_NOT_ADMINISTRATOR("S0100", "author.not.administrator"),

    ;

    private final String code;

    private final String message;

    UserErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
