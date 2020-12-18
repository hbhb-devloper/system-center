package com.hbhb.cw.systemcenter.enums.code;

import lombok.Getter;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Getter
public enum UserErrorCode {

    AUTHOR_NOT_ADMINISTRATOR("S0100", "author.not.administrator"),

    ORIGIN_PASSWORD_WRONG("S0102", "origin.password.wrong"),
    REPEAT_OF_USER_NAME("S0103", "repeat.of.user.name"),
    REPEAT_OF_NICK_NAME("S0104", "repeat.of.nick.name"),
    DEFAULT_UNIT_SET_ERROR("S0105", "default.unit.set.error"),
    ;

    private final String code;

    private final String message;

    UserErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
