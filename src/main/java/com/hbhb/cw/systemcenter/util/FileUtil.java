package com.hbhb.cw.systemcenter.util;

import com.hbhb.core.utils.NumberUtil;
import com.hbhb.cw.systemcenter.enums.code.FileErrorCode;
import com.hbhb.cw.systemcenter.exception.FileException;
import com.hbhb.cw.systemcenter.vo.FileVO;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wxg
 */
@Slf4j
public class FileUtil {

    /**
     * 上传多个文件
     */
    public static List<FileVO> uploadFileList(MultipartFile[] files, String filePath) {
        List<FileVO> list = new ArrayList<>();
        for (MultipartFile file : files) {
            list.add(uploadFile(file, filePath));
        }
        return list;
    }

    /**
     * 上传单个文件
     */
    public static FileVO uploadFile(MultipartFile file, String filePath) {
        if (file == null) {
            throw new FileException(FileErrorCode.UPLOAD_FILE_IS_EMPTY);
        }

        // 获取文件目录地址
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new FileException(FileErrorCode.UPLOAD_FILE_IS_EMPTY);
        }

        // 文件名重复性校验
        StringBuilder destFileName = new StringBuilder();
        StringBuilder destFilePath = new StringBuilder();
        int fileMax = getFileMax(filePath, fileName);
        if (fileMax > 0) {
            destFileName.append(fileName, 0, fileName.lastIndexOf("."))
                    .append("(").append(fileMax).append(")")
                    .append(fileName.substring(fileName.lastIndexOf(".")));
        } else {
            destFileName.append(fileName);
        }
        destFilePath.append(filePath).append(destFileName);

        // 开始上传文件
        try {
            Files.copy(file.getInputStream(), Paths.get(String.valueOf(destFilePath)));
            // 返回文件参数
            return FileVO.builder()
                    .fileName(destFileName.toString())
                    .fileSize(getLength(file.getSize()))
                    .build();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static void downloadFile(HttpServletResponse response, String filePath) {
        downloadFile(response, filePath, false);
    }

    public static void downloadFile(HttpServletResponse response, String filePath, Boolean deleteFile) {
        File file = new File(filePath);
        try {
            InputStream in = new FileInputStream(file);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-disposition", "attachment;filename=" + getFileName(filePath));
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            // 读取文件流
            int len;
            byte[] buffer = new byte[1024 * 10];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            in.close();
            // 是否删除文件
            if (deleteFile) {
                deleteFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据路径获取文件名
     */
    private static String getFileName(String filePath) {
        if (!StringUtils.isEmpty(filePath)) {
            String[] split = filePath.split("/");
            return split[split.length - 1];
        }
        return "";
    }

    /**
     * 递归删除指定文件或文件夹
     */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
        }
        file.delete();
    }

    private static int getFileMax(String filePath, String fileName) {
        File file = new File(filePath);
        File[] files;
        int number = 0;
        if (file.isDirectory()) {
            files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isFile() && (f.getName().substring(0, f.getName().lastIndexOf("."))
                            .contains(fileName.substring(0, fileName.lastIndexOf(".")))
                            && f.getName().substring(f.getName().lastIndexOf("."))
                            .equals(fileName.substring(fileName.lastIndexOf("."))))) {
                        number = number + 1;
                    }
                }
            }
        }
        return number;
    }

    public static String getLength(long filesize) {
        int byteLen = 1024;
        if (filesize < byteLen) {
            return filesize + "B";
        } else if (filesize < byteLen * byteLen) {
            return NumberUtil.format(((double) filesize) / byteLen) + "KB";
        } else if (filesize < byteLen * byteLen * byteLen) {
            return NumberUtil.format(((double) filesize) / (byteLen * byteLen)) + "MB";
        } else {
            return NumberUtil.format(((double) filesize) / (byteLen * byteLen * byteLen)) + "GB";
        }
    }
}
