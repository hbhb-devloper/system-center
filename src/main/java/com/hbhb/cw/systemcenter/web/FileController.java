package com.hbhb.cw.systemcenter.web;

import com.hbhb.cw.systemcenter.enums.FileType;
import com.hbhb.cw.systemcenter.enums.UserErrorCode;
import com.hbhb.cw.systemcenter.exception.UserException;
import com.hbhb.cw.systemcenter.service.FileService;
import com.hbhb.cw.systemcenter.service.SysRoleService;
import com.hbhb.cw.systemcenter.vo.FileDetailVO;
import com.hbhb.cw.systemcenter.vo.FileResVO;
import com.hbhb.web.annotation.UserId;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "文件上传")
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;
    @Resource
    private SysRoleService sysRoleService;

    @Operation(summary = "项目签报类型文件上传")
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public List<FileDetailVO> uploadFile(@RequestPart("files") MultipartFile[] files) {
        return fileService.upload(files);
    }

    @Operation(summary = "客户资金-发票预开类文件上传")
    @PostMapping(value = "/invoice", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public List<FileDetailVO> uploadFundInvoiceFile(@RequestPart("files") MultipartFile[] files) {
        return fileService.uploadFundInvoiceFile(files);
    }

    @Operation(summary = "系统文件上传")
    @PostMapping(value = "/system", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void uploadSystemFile(@RequestPart(required = false, value = "file") MultipartFile file,
                                 @Parameter(hidden = true) @UserId Integer userId) {
        if (sysRoleService.isAdminRole(userId)) {
            throw new UserException(UserErrorCode.AUTHOR_NOT_ADMINISTRATOR);
        }
        MultipartFile[] files = new MultipartFile[1];
        files[0] = file;
        fileService.uploadSystemFile(files);
    }

    @Operation(summary = "系统文件展示")
    @GetMapping("/list")
    public List<FileResVO> list() {
        return fileService.getFileList(FileType.SYSTEM_FILE.value());
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/delete/{id}")
    public void deleteFile(@Parameter(description = "文件id") @PathVariable Long id,
                           @Parameter(hidden = true) @UserId Integer userId) {
        if (sysRoleService.isAdminRole(userId)) {
            throw new UserException(UserErrorCode.AUTHOR_NOT_ADMINISTRATOR);
        }
        fileService.deleteFile(id);
    }
}
