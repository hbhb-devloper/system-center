package com.hbhb.cw.systemcenter.model;

import org.beetl.sql.annotation.entity.AutoID;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dxk
 * @since 2020-09-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dict implements Serializable {
    private static final long serialVersionUID = 5853187740251669853L;

    @AutoID
    private Integer id;
    @Schema(description = "字典类型id")
    private Integer dictTypeId;
    @Schema(description = "字典标签")
    private String dictLabel;
    @Schema(description = "字典键值")
    private String dictValue;
    @Schema(description = "字典编码")
    private String dictCode;
    @Schema(description = "显示顺序")
    private Integer sortNum;
    @Schema(description = "状态（0-停用、1-正常）")
    private Integer state;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "创建时间")
    private Date createTime;
    @Schema(description = "创建人")
    private String createBy;
}
