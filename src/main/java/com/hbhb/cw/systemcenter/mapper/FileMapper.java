package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.beetlsql.BaseMapper;
import com.hbhb.cw.systemcenter.model.File;
import com.hbhb.cw.systemcenter.web.vo.FileResVO;

import java.util.List;

/**
 * @author dxk
 * @since 2020-09-29
 */
public interface FileMapper extends BaseMapper<File> {

    List<FileResVO> selectListByType(Integer type);
}