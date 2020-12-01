package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.bean.BeanConverter;
import com.hbhb.cw.systemcenter.mapper.FileMapper;
import com.hbhb.cw.systemcenter.model.File;
import com.hbhb.cw.systemcenter.service.FileService;
import com.hbhb.cw.systemcenter.util.FileUtil;
import com.hbhb.cw.systemcenter.vo.FileVO;
import com.hbhb.cw.systemcenter.web.vo.FileResVO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileMapper fileMapper;

    @Value("${file.download.domain}")
    private String fileDomain;
    @Value("${file.upload.path}")
    private String filePath;

    @Override
    public List<FileVO> uploadList(MultipartFile[] multipartFile, Integer bizType) {
        Date now = new Date();
        List<File> files = new ArrayList<>();
        List<FileVO> voList = FileUtil.uploadFileList(multipartFile, filePath);
        voList.forEach(vo -> files.add(File.builder()
                .fileName(vo.getFileName())
                .filePath(fileDomain + java.io.File.separator + vo.getFileName())
                .fileSize(vo.getFileSize())
                .uploadTime(now)
                .bizType(bizType)
                .build()));
        // 保存文件信息
        fileMapper.insertBatch(files);
        // 返回前端所需的文件信息
        Map<String, Long> fileMap = files.stream().collect(
                Collectors.toMap(File::getFileName, File::getId));
        voList.forEach(vo -> {
            if (fileMap.containsKey(vo.getFileName())) {
                vo.setId(fileMap.get(vo.getFileName()));
            }
        });
        return voList;
    }

    @Override
    public FileVO upload(MultipartFile file, Integer bizType) {
        FileVO vo = FileUtil.uploadFile(file, filePath);
        if (vo != null) {
            File insertFile = File.builder()
                    .fileName(vo.getFileName())
                    .filePath(fileDomain + java.io.File.separator + vo.getFileName())
                    .fileSize(vo.getFileSize())
                    .uploadTime(new Date())
                    .bizType(bizType)
                    .build();
            // 保存文件信息
            fileMapper.insert(insertFile);
            // 将主键id返回
            vo.setId(insertFile.getId());
        }
        return vo;
    }

    @Override
    public List<FileResVO> getFileList(Integer type) {
        List<File> list = fileMapper.createLambdaQuery()
                .andEq(File::getBizType, type)
                .select();
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(file -> BeanConverter.convert(file, FileResVO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteFile(Long id) {
        fileMapper.deleteById(id);
    }

    @Override
    public List<File> getFileInfoBatch(List<Integer> list) {
        return fileMapper.selectByIds(list);
    }

    @Override
    public File getFile(Integer fileId) {
        return fileMapper.lock(fileId);
    }
}