package com.hbhb.cw.systemcenter.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintainVO implements Serializable {
    private static final long serialVersionUID = 2476219145226922967L;

    @Schema(description = "管理员姓名")
    private String administrator;
    @Schema(description = "联系方式")
    private String phone;
    @Schema(description = "管理员邮箱")
    private String email;
    @Schema(description = "软件名称")
    private String softwareName;
    @Schema(description = "版本")
    private String version;
    @Schema(description = "开发语言")
    private String devLanguage;
}
