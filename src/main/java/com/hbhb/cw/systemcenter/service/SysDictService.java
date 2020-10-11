package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.SysDict;
import com.hbhb.cw.systemcenter.vo.SysDictResVO;
import com.hbhb.cw.systemcenter.vo.SysDictVO;
import com.hbhb.springboot.webflux.view.Page;

import java.util.List;

/**
 * @author dxk
 */
public interface SysDictService {

    /**
     * 分页查询字典列表
     */
    Page<SysDictResVO> pageDictByCond(long pageNum, long pageSize,
                                      String dictTypeName, String dictLabel);

    /**
     * 按条件查询对应的所有字典数据
     */
    List<SysDictVO> listDictByCond(String dictType, String dictCode);

    /**
     * 获取字典详情
     */
    SysDict getDictInfo(Integer id);

    /**
     * 新增/更新字典
     */
    void upsertDict(SysDict sysDict);

    /**
     * 删除字典
     */
    void deleteDict(Integer id);
}
