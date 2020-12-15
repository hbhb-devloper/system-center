package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.SysFile;
import com.hbhb.cw.systemcenter.vo.FileVO;
import com.hbhb.cw.systemcenter.web.vo.FileResVO;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

public interface SysFileService {

    /**
     * 文件上传
     */
    FileVO upload(MultipartFile files, Integer bizType);

    /**
     * 批量文件上传
     */
    List<FileVO> uploadBatch(MultipartFile[] files, Integer bizType);

    /**
     * 下载文件 .
     */
    void download(HttpServletResponse response, String filePath, Boolean deleteFile);

    /**
     * 填充文件模板
     */
    void fillTemplate(Object data, String templateName, String filePath);

    /**
     * 跟据类型获取文件列表
     */
    List<FileResVO> getFileList(Integer type);

    /**
     * 根据id查询文件信息
     */
    SysFile getFileInfo(Integer fileId);

    /**
     * 根据id批量查询文件信息
     */
    List<SysFile> getFileInfoBatch(List<Integer> list);

    /**
     * 获取文件存放路径
     */
    String getFilePath();

    /**
     * 删除文件
     */
    void deleteFile(Long id);
}
