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
public class DictResVO implements Serializable {
    private static final long serialVersionUID = -5638062399865717716L;

    private Integer id;
    @Schema(description = "字典类型名称")
    private String dictTypeName;
    @Schema(description = "字典标签")
    private String dictLabel;
    @Schema(description = "字典键值")
    private String dictValue;
    @Schema(description = "字典编码")
    private String dictCode;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "显示顺序")
    private Integer sortNum;
    @Schema(description = "状态（0-停用、1-正常）")
    private Integer state;
}
