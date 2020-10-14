package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.SysDict;
import com.hbhb.cw.systemcenter.vo.SysDictResVO;

import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.mapper.BaseMapper;

import java.util.List;

/**
 * @author dxk
 * @since 2020-09-29
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    void selectPageByCond(PageQuery<SysDictResVO> query, String dictTypeName, String dictLabel);

    List<SysDict> selectListByCond(String dictType, String dictCode);
}