package com.hbhb.cw.systemcenter.model;

import org.beetl.sql.annotation.entity.AutoID;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Broadcast implements Serializable {
    private static final long serialVersionUID = -2967992542095864782L;

    @AutoID
    private Long id;
    @Schema(description = "公告内容")
    private String content;
    @Schema(description = "显示顺序")
    private Integer sortNum;
    @Schema(description = "公告状态（0-停用、1-启用）")
    private Byte state;
    @Schema(description = "创建人")
    private Integer createBy;
    @Schema(description = "创建时间")
    private Date createTime;
}