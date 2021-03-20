package com.hbhb.cw.systemcenter.exception;

import com.hbhb.core.bean.MessageConvert;
import com.hbhb.cw.systemcenter.enums.code.UserErrorCode;
import com.hbhb.web.exception.BusinessException;

import lombok.Getter;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Getter
public class UserException extends BusinessException {
    private static final long serialVersionUID = -9139987747932274050L;

    private final String code;

    public UserException(UserErrorCode errorCode) {
        super(errorCode.getCode(), MessageConvert.convert(errorCode.getMessage()));
        this.code = errorCode.getCode();
    }
}
