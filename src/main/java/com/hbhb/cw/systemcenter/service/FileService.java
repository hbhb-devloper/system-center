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
    List<FileVO> uploadList(MultipartFile[] files, Integer bizType);

    /**
     * 跟据类型获取文件列表
     */
    List<FileResVO> getFileList(Integer type);

    /**
     * 跟据id删除文件
     */
    void deleteFile(Long id);

    /**
     * 按文件id批量获取附件信息
     */
    List<File> getFileInfoBatch(List<Integer> list);

    /**
     * 跟据文件id获取文件信息
     *
     * @return
     */
    File getFile(Integer fileId);
}
