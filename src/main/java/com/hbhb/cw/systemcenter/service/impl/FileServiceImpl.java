package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.bean.BeanConverter;
import com.hbhb.cw.systemcenter.mapper.FileMapper;
import com.hbhb.cw.systemcenter.model.File;
import com.hbhb.cw.systemcenter.service.FileService;
import com.hbhb.cw.systemcenter.util.FileUtil;
import com.hbhb.cw.systemcenter.vo.FileVO;
import com.hbhb.cw.systemcenter.web.vo.FileResVO;

import freemarker.template.Configuration;
import freemarker.template.Template;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Resource
    private FileMapper fileMapper;
    @Resource
    private Configuration configuration;

    @Value("${cw.file.domain}")
    private String fileDomain;
    @Value("${cw.file.path}")
    private String filePath;

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
    public List<FileVO> uploadBatch(MultipartFile[] multipartFile, Integer bizType) {
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
    public void download(HttpServletResponse response, String filePath, Boolean deleteFile) {
        FileUtil.downloadFile(response, filePath, deleteFile);
    }

    @Override
    public void fillTemplate(Object data, String templateName, String filePath) {
        java.io.File file = new java.io.File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             Writer out = new BufferedWriter(osw)) {
            configuration.setDirectoryForTemplateLoading(
                    new java.io.File(this.filePath + "template"));
            Template t = configuration.getTemplate(templateName);
            t.process(data, out);
        } catch (Exception e) {
            log.error("填充[{}]模板数据失败", templateName, e);
        }
    }

    @Override
    public List<FileResVO> getFileList(Integer type) {
        List<File> files = fileMapper.createLambdaQuery()
                .andEq(File::getBizType, type)
                .select();
        return Optional.ofNullable(files)
                .orElse(new ArrayList<>())
                .stream()
                .map(file -> BeanConverter.convert(file, FileResVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public File getFileInfo(Integer fileId) {
        return fileMapper.single(fileId);
    }

    @Override
    public List<File> getFileInfoBatch(List<Integer> list) {
        return fileMapper.selectByIds(list);
    }

    @Override
    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public void deleteFile(Long id) {
        fileMapper.deleteById(id);
    }
}