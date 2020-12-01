package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.cw.systemcenter.api.FileApi;
import com.hbhb.cw.systemcenter.enums.FileType;
import com.hbhb.cw.systemcenter.model.File;
import com.hbhb.cw.systemcenter.service.FileService;
import com.hbhb.cw.systemcenter.vo.FileVO;
import com.hbhb.cw.systemcenter.web.vo.FileResVO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangxiaogang
 */
@Tag(name = "文件上传")
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController implements FileApi {

    @Resource
    private FileService fileService;

    @Value("${file.upload.template}")
    private String filePath;

    @Operation(summary = "文件上传")
    @Override
    public FileVO uploadFile(MultipartFile file,
                             @Parameter(description = "业务类型") Integer bizType) {
        bizType = bizType == null ? FileType.SYSTEM_FILE.value() : bizType;
        return fileService.upload(file, bizType);
    }

    @Operation(summary = "批量文件上传")
    @Override
    public List<FileVO> uploadFileBatch(MultipartFile[] files,
                                        @Parameter(description = "业务类型") Integer bizType) {
        bizType = bizType == null ? FileType.SYSTEM_FILE.value() : bizType;
        return fileService.uploadList(files, bizType);
    }

    @Operation(summary = "获取指定类型的文件列表")
    @GetMapping("/list")
    public List<FileResVO> listByType(
            @Parameter(description = "业务类型") @RequestParam Integer bizType) {
        return fileService.getFileList(bizType);
    }

    @Operation(summary = "根据id批量查询文件信息")
    @Override
    public List<File> getFileInfo(List<Integer> fileIds) {
        return fileService.getFileInfoBatch(fileIds);
    }

    @Operation(summary = "根据id文件信息")
    @Override
    public File getFile(Integer fileId) {
        return fileService.getFile(fileId);
    }

    @Operation(summary = "获取文件模板路径")
    @Override
    public String getFileTemplatePath() {
        return filePath;
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/{fileId}")
    public void deleteFile(@Parameter(description = "文件id") @PathVariable Long fileId) {
        fileService.deleteFile(fileId);
    }
}
