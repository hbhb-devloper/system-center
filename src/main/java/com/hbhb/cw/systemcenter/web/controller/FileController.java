package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.cw.systemcenter.api.FileApi;
import com.hbhb.cw.systemcenter.enums.FileType;
import com.hbhb.cw.systemcenter.model.File;
import com.hbhb.cw.systemcenter.service.FileService;
import com.hbhb.cw.systemcenter.vo.FileVO;
import com.hbhb.cw.systemcenter.web.vo.FileResVO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangxiaogang
 */
@Tag(name = "文件")
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController implements FileApi {

    @Resource
    private FileService fileService;

    @Operation(summary = "文件上传")
    @Override
    public FileVO upload(MultipartFile file, @Parameter(description = "业务类型") Integer bizType) {
        bizType = bizType == null ? FileType.SYSTEM_FILE.value() : bizType;
        return fileService.upload(file, bizType);
    }

    @Operation(summary = "批量文件上传")
    @Override
    public List<FileVO> uploadBatch(MultipartFile[] files, @Parameter(description = "业务类型") Integer bizType) {
        bizType = bizType == null ? FileType.SYSTEM_FILE.value() : bizType;
        return fileService.uploadBatch(files, bizType);
    }

    @Operation(summary = "下载文件")
    @Override
    public void download(HttpServletResponse response,
                         @Parameter(description = "文件路径+名称", example = "/app/file/test.xlsx") String filePath,
                         @Parameter(description = "是否删除原文件") Boolean deleteFile) {
        fileService.download(response, filePath, deleteFile != null && deleteFile);
    }

    @Operation(summary = "填充指定freemarker模板，并生成文件")
    @Override
    public void fillTemplate(@Parameter(description = "填充数据") Object data,
                             @Parameter(description = "模板名称") String templateName,
                             @Parameter(description = "生成的文件路径", example = "/app/file/test.xlsx") String filePath) {
        fileService.fillTemplate(data, templateName, filePath);
    }

    @Operation(summary = "获取指定类型的文件列表",
            description = "10-系统文件、20-预算执行-签报类附件、30-客户资金-发票类附件、40-迁改-预警类附件、50-迁改-合同类附件")
    @GetMapping("/list")
    public List<FileResVO> getFileList(
            @Parameter(description = "业务类型") @RequestParam(required = false) Integer bizType) {
        bizType = bizType == null ? FileType.SYSTEM_FILE.value() : bizType;
        return fileService.getFileList(bizType);
    }

    @Operation(summary = "根据id查询文件信息")
    @Override
    public File getFileInfo(Integer fileId) {
        return fileService.getFileInfo(fileId);
    }

    @Operation(summary = "根据id批量查询文件信息")
    @Override
    public List<File> getFileInfoBatch(List<Integer> fileIds) {
        return fileService.getFileInfoBatch(fileIds);
    }

    @Operation(summary = "获取文件存放路径")
    @Override
    public String getPath() {
        return fileService.getFilePath();
    }

    @Operation(summary = "获取文件模板存放路径")
    @Override
    public String getTemplatePath() {
        return fileService.getFilePath() + "template";
    }

    @Operation(summary = "删除文件")
    @Override
    public void deleteFile(@Parameter(description = "文件id") Long fileId) {
        fileService.deleteFile(fileId);
    }
}
