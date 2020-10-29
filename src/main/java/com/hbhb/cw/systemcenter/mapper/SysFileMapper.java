package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.SysFile;
import com.hbhb.cw.systemcenter.vo.FileResVO;

import org.beetl.sql.mapper.BaseMapper;

import java.util.List;

/**
 * @author dxk
 * @since 2020-09-29
 */
public interface SysFileMapper extends BaseMapper<SysFile> {

    List<FileResVO> selectListByType(Integer type);
}