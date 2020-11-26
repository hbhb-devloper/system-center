package com.hbhb.cw.systemcenter.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaokang
 * @since 2020-09-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictTypeVO implements Serializable {
    private static final long serialVersionUID = 9093574899531998987L;

    private Integer id;
    @Schema(description = "类型名称")
    private String typeName;
    @Schema(description = "类型编码")
    private String typeCode;
    @Schema(description = "状态（0-停用、1-正常）")
    private Integer state;
    @Schema(description = "备注")
    private String remark;
}
