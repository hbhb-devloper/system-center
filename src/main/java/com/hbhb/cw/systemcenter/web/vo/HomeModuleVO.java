package com.hbhb.cw.systemcenter.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wxg
 * @since 2020-09-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeModuleVO {

    @Schema(description = "模块值")
    private Integer module;
    @Schema(description = "模块名称")
    private String moduleName;
    @Schema(description = "统计值")
    private Long count;
}
