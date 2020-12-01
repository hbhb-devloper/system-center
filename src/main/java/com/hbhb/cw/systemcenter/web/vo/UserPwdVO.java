package com.hbhb.cw.systemcenter.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaokang
 * @since 2020-08-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPwdVO implements Serializable {
    private static final long serialVersionUID = -5622255938996429828L;

    @Schema(description = "旧密码")
    private String oldPwd;
    @Schema(description = "新密码")
    private String newPwd;
}
