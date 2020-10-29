package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.enums.FileType;
import com.hbhb.cw.systemcenter.mapper.SysFileMapper;
import com.hbhb.cw.systemcenter.model.SysFile;
import com.hbhb.cw.systemcenter.service.FileService;
import com.hbhb.cw.systemcenter.util.FileUtil;
import com.hbhb.cw.systemcenter.vo.FileDetailVO;
import com.hbhb.cw.systemcenter.vo.FileResVO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

@Service
public class FileServiceImpl implements FileService {

    @Resource
    private SysFileMapper sysFileMapper;

    @Value("${file.download.domain}")
    private String fileDomain;
    @Value("${file.upload.path}")
    private String filePath;

    @Override
    public List<FileDetailVO> upload(MultipartFile[] multipartFile) {
        Date now = new Date();
        List<SysFile> files = new ArrayList<>();
        List<FileDetailVO> voList = FileUtil.uploadFileList(multipartFile, filePath);
        voList.forEach(vo -> files.add(SysFile.builder()
                .fileName(vo.getFileName())
                .filePath(fileDomain + File.separator + vo.getFileName())
                .fileSize(vo.getFileSize())
                .uploadTime(now)
                .bizType(FileType.BUDGET_PROJECT_FILE.value())
                .build()));
        // 保存文件信息
        sysFileMapper.insertBatch(files);
        // 返回前端所需的文件信息
        Map<String, Long> fileMap = files.stream().collect(
                Collectors.toMap(SysFile::getFileName, SysFile::getId));
        voList.forEach(vo -> {
            if (fileMap.containsKey(vo.getFileName())) {
                vo.setId(fileMap.get(vo.getFileName()));
            }
        });
        return voList;
    }

    @Override
    public List<FileDetailVO> uploadFundInvoiceFile(MultipartFile[] multipartFile) {
        List<SysFile> files = new ArrayList<>();
        List<FileDetailVO> voList = FileUtil.uploadFileList(multipartFile, filePath);
        voList.forEach(vo -> files.add(SysFile.builder()
                .fileName(vo.getFileName())
                .filePath(fileDomain + File.separator + vo.getFileName())
                .fileSize(vo.getFileSize())
                .uploadTime(new Date())
                .bizType(FileType.FUND_INVOICE_FILE.value())
                .build()));
        // 保存文件信息
        sysFileMapper.insertBatch(files);
        // 返回前端所需的文件信息
        Map<String, Long> fileMap = files.stream().collect(
                Collectors.toMap(SysFile::getFileName, SysFile::getId));
        voList.forEach(vo -> {
            if (fileMap.containsKey(vo.getFileName())) {
                vo.setId(fileMap.get(vo.getFileName()));
            }
        });
        return voList;
    }

    @Override
    public void uploadSystemFile(MultipartFile[] multipartFile) {
        Date now = new Date();
        List<SysFile> files = new ArrayList<>();

        List<FileDetailVO> voList = FileUtil.uploadFileList(multipartFile, filePath);
        voList.forEach(vo -> files.add(SysFile.builder()
                .fileName(vo.getFileName())
                .filePath(fileDomain + File.separator + vo.getFileName())
                .fileSize(vo.getFileSize())
                .uploadTime(now)
                .bizType(FileType.SYSTEM_FILE.value())
                .build()));
        // 保存文件信息
        sysFileMapper.insertBatch(files);
    }

    @Override
    public List<FileResVO> getFileList(Integer type) {
        return sysFileMapper.selectListByType(type);
    }

    @Override
    public void deleteFile(Long id) {
        sysFileMapper.deleteById(id);
    }
}