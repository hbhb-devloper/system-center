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
public class DictType implements Serializable {
    private static final long serialVersionUID = 3534456777087071154L;
    @AutoID
    private Integer id;
    @Schema(description = "类型名称")
    private String typeName;
    @Schema(description = "类型编码")
    private String typeCode;
    @Schema(description = "状态（0-停用、1-正常）")
    private Integer state;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "创建时间")
    private Date createTime;
    @Schema(description = "创建人")
    private String createBy;
}
