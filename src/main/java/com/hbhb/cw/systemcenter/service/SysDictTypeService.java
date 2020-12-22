package com.hbhb.cw.systemcenter.service;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.cw.systemcenter.model.SysDictType;

import org.beetl.sql.core.page.PageResult;

import java.util.List;

/**
 * @author dxk
 */
public interface SysDictTypeService {

    /**
     * 分页查询字典类型列表
     */
    PageResult<SysDictType> pageDictTypeByCond(Long pageNum, Integer pageSize);

    /**
     * 获取所有字典类型
     */
    List<SelectVO> getAllDictType();

    /**
     * 获取字典类型详情
     */
    SysDictType getDictTypeInfo(Integer dictTypeId);

    /**
     * 新增/更新字典类型
     */
    void upsertDictType(SysDictType dictType);

    /**
     * 删除字典类型
     */
    void deleteDictType(Integer id);
}
