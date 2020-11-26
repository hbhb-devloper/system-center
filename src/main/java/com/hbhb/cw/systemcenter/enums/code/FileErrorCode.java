package com.hbhb.cw.systemcenter.enums.code;

import lombok.Getter;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Getter
public enum FileErrorCode {

    UPLOAD_FILE_IS_EMPTY("S0001", "upload.file.is.empty"),
    UPLOAD_FILE_NAME_IS_NULL("S0002", "upload.file.name.is.null"),
    ;

    private final String code;

    private final String message;

    FileErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
