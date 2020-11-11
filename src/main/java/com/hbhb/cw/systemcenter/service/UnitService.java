package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.Unit;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;

import java.util.List;

public interface UnitService {
    /**
     * 查询所有单位列表（KV）
     */
    List<Unit> getAllUnitList();

    /**
     * 获取用户的单位范围列表（下拉框用、KV）
     */
    List<TreeSelectVO> getUnitTreeSelectByUser(Integer userId);

}
