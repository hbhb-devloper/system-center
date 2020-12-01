package com.hbhb.cw.systemcenter.web.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaokang
 * @since 2020-06-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReqVO implements Serializable {
    private static final long serialVersionUID = 4122315174437563258L;

    @Schema(description = "单位id")
    private List<Integer> unitIds;
    @Schema(description = "登录名")
    private String userName;
    @Schema(description = "用户名")
    private String nickName;
    @Schema(description = "手机号")
    private String phone;
    @Schema(description = "状态")
    private Byte state;
}
