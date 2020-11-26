package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.DictType;

import org.beetl.sql.core.page.PageResult;

/**
 * @author dxk
 */
public interface DictTypeService {

    /**
     * 分页查询字典类型列表
     */
    PageResult<DictType> pageDictTypeByCond(Long pageNum, Integer pageSize);

    /**
     * 新增/更新字典类型
     */
    void upsertDictType(DictType dictType);

    /**
     * 删除字典类型
     */
    void deleteDictType(Integer id);
}
