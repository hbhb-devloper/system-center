package com.hbhb.cw.systemcenter.service;

import com.hbhb.core.bean.SelectVO;
import com.hbhb.cw.systemcenter.model.DictType;

import org.beetl.sql.core.page.PageResult;

import java.util.List;

/**
 * @author dxk
 */
public interface DictTypeService {

    /**
     * 分页查询字典类型列表
     */
    PageResult<DictType> pageDictTypeByCond(Long pageNum, Integer pageSize);

    /**
     * 获取所有字典类型
     */
    List<SelectVO> getAllDictType();

    /**
     * 获取字典类型详情
     */
    DictType getDictTypeInfo(Integer dictTypeId);

    /**
     * 新增/更新字典类型
     */
    void upsertDictType(DictType dictType);

    /**
     * 删除字典类型
     */
    void deleteDictType(Integer id);
}
