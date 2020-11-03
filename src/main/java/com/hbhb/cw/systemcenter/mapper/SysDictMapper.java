package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.SysDict;
import com.hbhb.cw.systemcenter.vo.SysDictResVO;
import com.hbhb.web.beetlsql.BaseMapper;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;

import java.util.List;

/**
 * @author dxk
 * @since 2020-09-29
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    PageResult<SysDictResVO> selectPageByCond(String dictTypeName, String dictLabel, PageRequest request);

    List<SysDict> selectListByCond(String dictType, String dictCode);
}