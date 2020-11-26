package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.DictType;
import com.hbhb.web.beetlsql.BaseMapper;

import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;

/**
 * @author dxk
 * @since 2020-09-29
 */
public interface DictTypeMapper extends BaseMapper<DictType> {

    PageResult<DictType> selectPageByCond(PageRequest request);

}