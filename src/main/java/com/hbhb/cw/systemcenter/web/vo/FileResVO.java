package com.hbhb.cw.systemcenter.web.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResVO implements Serializable {
    private static final long serialVersionUID = -2802858991745967518L;

    private Long id;
    @Schema(description = "文件名称")
    private String fileName;
    @Schema(description = "文件路径")
    private String filePath;
    @Schema(description = "上传时间")
    private String uploadTime;
}
