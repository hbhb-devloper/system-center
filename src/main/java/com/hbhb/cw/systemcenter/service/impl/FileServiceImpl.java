package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.mapper.SysFileMapper;
import com.hbhb.cw.systemcenter.model.SysFile;
import com.hbhb.cw.systemcenter.service.FileService;
import com.hbhb.cw.systemcenter.util.FileUtil;
import com.hbhb.cw.systemcenter.vo.FileDetailVO;
import com.hbhb.cw.systemcenter.vo.FileResVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    @Resource
    private SysFileMapper sysFileMapper;

    @Value("${file.download.domain}")
    private String fileDomain;
    @Value("${file.upload.path}")
    private String filePath;

    @Override
    public List<FileDetailVO> uploadList(MultipartFile[] multipartFile, Integer bizType) {
        Date now = new Date();
        List<SysFile> files = new ArrayList<>();
        List<FileDetailVO> voList = FileUtil.uploadFileList(multipartFile, filePath);
        voList.forEach(vo -> files.add(SysFile.builder()
                .fileName(vo.getFileName())
                .filePath(fileDomain + File.separator + vo.getFileName())
                .fileSize(vo.getFileSize())
                .uploadTime(now)
                .bizType(bizType)
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
    public FileDetailVO upload(MultipartFile file,Integer bizType) {
        SysFile files = new SysFile();
        FileDetailVO vo = FileUtil.uploadFile(file, filePath);
        if (vo != null) {
            SysFile.builder()
                    .fileName(vo.getFileName())
                    .filePath(fileDomain + File.separator + vo.getFileName())
                    .fileSize(vo.getFileSize())
                    .uploadTime(new Date())
                    .bizType(bizType)
                    .build();
        }
        // 保存文件信息
        sysFileMapper.insert(files);
        // 返回前端所需的文件信息
        return vo;
    }

    @Override
    public List<FileResVO> getFileList(Integer type) {
        return sysFileMapper.selectListByType(type);
    }

    @Override
    public void deleteFile(Long id) {
        sysFileMapper.deleteById(id);
    }

    @Override
    public void remove(File file) {
        FileUtil.deleteFile(file);
    }
}