package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.File;
import com.hbhb.cw.systemcenter.vo.FileVO;
import com.hbhb.cw.systemcenter.web.vo.FileResVO;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    /**
     * 文件上传
     */
    FileVO upload(MultipartFile files, Integer bizType);

    /**
     * 批量文件上传
     */
    List<FileVO> uploadBatch(MultipartFile[] files, Integer bizType);

    /**
     * 跟据类型获取文件列表
     */
    List<FileResVO> getFileList(Integer type);

    /**
     * 根据id查询文件信息
     */
    File getFileInfo(Integer fileId);

    /**
     * 根据id批量查询文件信息
     */
    List<File> getFileInfoBatch(List<Integer> list);

    /**
     * 删除文件
     */
    void deleteFile(Long id);
}
