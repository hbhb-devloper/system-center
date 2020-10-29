package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.vo.FileDetailVO;
import com.hbhb.cw.systemcenter.vo.FileResVO;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    /**
     * 项目签报附件上传
     */
    List<FileDetailVO> upload(MultipartFile[] files);

    /**
     * 客户资金-发票类附件上传
     */
    List<FileDetailVO> uploadFundInvoiceFile(MultipartFile[] files);

    /**
     * 系统文件上传
     */
    void uploadSystemFile(MultipartFile[] files);

    /**
     * 跟据类型获取文件列表
     */
    List<FileResVO> getFileList(Integer type);

    /**
     * 跟据id删除文件
     */
    void deleteFile(Long id);

}
