package com.hbhb.cw.systemcenter.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaokang
 * @since 2020-09-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BroadcastVO implements Serializable {
    private static final long serialVersionUID = -8587532302686556146L;

    private Long id;
    @Schema(description = "公告内容")
    private String content;
    @Schema(description = "显示顺序")
    private Integer sortNum;
    @Schema(description = "公告状态（0-停用、1-启用）")
    private Byte state;
}
