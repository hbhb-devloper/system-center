package com.hbhb.cw.systemcenter.web;

import com.hbhb.cw.systemcenter.api.SysFileApi;
import com.hbhb.cw.systemcenter.enums.FileType;
import com.hbhb.cw.systemcenter.model.SysFile;
import com.hbhb.cw.systemcenter.service.FileService;
import com.hbhb.cw.systemcenter.vo.FileDetailVO;
import com.hbhb.cw.systemcenter.vo.FileResVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @author wangxiaogang
 */
@Tag(name = "文件上传")
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController implements SysFileApi {

    @Resource
    private FileService fileService;

    @Value("${file.upload.template}")
    private String filePath;

    @Override
    @Operation(summary = "多文件上传")
    public List<FileDetailVO> uploadFileList(MultipartFile[] multipartFiles, Integer bizType) {
        bizType = bizType == null ? FileType.SYSTEM_FILE.value() : bizType;
        return fileService.uploadList(multipartFiles, bizType);
    }

    @Override
    public FileDetailVO uploadFile(MultipartFile file, Integer bizType) {
        bizType = bizType == null ? FileType.SYSTEM_FILE.value() : bizType;
        return fileService.upload(file, bizType);
    }

    @Operation(summary = "文件列表")
    @Override
    public List<FileResVO> list(Integer integer) {
        return fileService.getFileList(integer);
    }

    @Operation(summary = "删除文件")
    @Override
    public void deleteFile(Long id) {
        fileService.deleteFile(id);
    }

    @Operation(summary = "移除文件")
    @Override
    public void remove(File file) {
        fileService.remove(file);
    }

    @Operation(summary = "根据文件id批量查询文件信息")
    @Override
    public List<SysFile> getFileList(List<Integer> fileIds) {
        return fileService.getFileInfoList(fileIds);
    }

    @Override
    public String getFileTemplatePath() {
        return filePath;
    }
}
