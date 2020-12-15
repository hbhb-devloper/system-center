package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.beetlsql.BaseMapper;
import com.hbhb.cw.systemcenter.model.SysDict;
import com.hbhb.cw.systemcenter.web.vo.DictResVO;

import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;

import java.util.List;

/**
 * @author dxk
 * @since 2020-09-29
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    PageResult<DictResVO> selectPageByCond(Integer dictTypeId, String dictTypeName, String dictLabel,
                                           PageRequest request);

    List<SysDict> selectListByCond(String dictType, String dictCode);
}