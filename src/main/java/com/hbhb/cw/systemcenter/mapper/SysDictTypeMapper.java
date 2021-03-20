package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.beetlsql.BaseMapper;
import com.hbhb.cw.systemcenter.model.SysDictType;

import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;

/**
 * @author dxk
 * @since 2020-09-29
 */
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {

    PageResult<SysDictType> selectPageByCond(PageRequest request);

}