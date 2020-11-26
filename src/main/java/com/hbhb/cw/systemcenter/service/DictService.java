package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.Dict;
import com.hbhb.cw.systemcenter.vo.DictVO;
import com.hbhb.cw.systemcenter.web.vo.DictResVO;

import org.beetl.sql.core.page.PageResult;

import java.util.List;

/**
 * @author dxk
 */
public interface DictService {

    /**
     * 分页查询字典列表
     */
    PageResult<DictResVO> pageDictByCond(Long pageNum, Integer pageSize,
                                         String dictTypeName, String dictLabel);

    /**
     * 按条件查询对应的所有字典数据
     */
    List<DictVO> listDictByCond(String dictType, String dictCode);

    /**
     * 获取字典详情
     */
    Dict getDictInfo(Integer id);

    /**
     * 新增/更新字典
     */
    void upsertDict(Dict dict);

    /**
     * 删除字典
     */
    void deleteDict(Integer id);
}
