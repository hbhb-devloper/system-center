package com.hbhb.cw.systemcenter.web;

import com.hbhb.cw.systemcenter.api.SysFileApi;
import com.hbhb.cw.systemcenter.service.FileService;
import com.hbhb.cw.systemcenter.vo.FileDetailVO;
import com.hbhb.cw.systemcenter.vo.FileResVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Tag(name = "文件上传")
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController implements SysFileApi {

    @Resource
    private FileService fileService;

    @Override
    @Operation(summary = "多文件上传")
    public List<FileDetailVO> uploadFileList(MultipartFile[] multipartFiles, Integer bizType) {
        return fileService.uploadList(multipartFiles, bizType);
    }

    @Override
    public FileDetailVO uploadFile(MultipartFile file, Integer bizType) {
        return fileService.upload(file,bizType);
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
}
