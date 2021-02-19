package com.hbhb.cw.systemcenter.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wangxiaogang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordVO implements Serializable {
    @Schema(description = "OA邮箱")
    private String email;
    @Schema(description = "新密码")
    private String newPwd;
}
