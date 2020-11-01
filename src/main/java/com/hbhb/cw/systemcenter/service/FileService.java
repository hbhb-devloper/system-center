package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.vo.FileDetailVO;
import com.hbhb.cw.systemcenter.vo.FileResVO;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface FileService {

    /**
     * 多附件上传
     */
    List<FileDetailVO> uploadList(MultipartFile[] files,Integer bizType);

    /**
     * 单文件上传
     */
    FileDetailVO upload(MultipartFile files,Integer bizType);

    /**
     * 跟据类型获取文件列表
     */
    List<FileResVO> getFileList(Integer type);

    /**
     * 跟据id删除文件
     */
    void deleteFile(Long id);

    /**
     * 移除文件
     */
    void remove(File file);

}
