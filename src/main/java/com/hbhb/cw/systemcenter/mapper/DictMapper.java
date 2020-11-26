package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.Dict;
import com.hbhb.cw.systemcenter.web.vo.DictResVO;
import com.hbhb.web.beetlsql.BaseMapper;

import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;

import java.util.List;

/**
 * @author dxk
 * @since 2020-09-29
 */
public interface DictMapper extends BaseMapper<Dict> {

    PageResult<DictResVO> selectPageByCond(String dictTypeName, String dictLabel, PageRequest request);

    List<Dict> selectListByCond(String dictType, String dictCode);
}