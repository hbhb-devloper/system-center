package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.beetlsql.BaseMapper;
import com.hbhb.cw.systemcenter.model.SysBroadcast;

import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;

/**
 * @author dxk
 * @since 2020-09-29
 */
public interface BroadcastMapper extends BaseMapper<SysBroadcast> {

    PageResult<SysBroadcast> selectPageByCond(String content, Byte state, PageRequest request);

}