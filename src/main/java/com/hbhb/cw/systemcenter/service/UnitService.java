package com.hbhb.cw.systemcenter.service;

import com.hbhb.core.bean.SelectVO;
import com.hbhb.cw.systemcenter.model.Unit;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;

import java.util.List;
import java.util.Map;

public interface UnitService {

    /**
     * 获取所有单位列表（树形结构、KV）
     */
    List<TreeSelectVO> getAllUnitTreeList();

    /**
     * 获取角色所对应的单位id
     */
    List<Integer> getCheckedUnitByRole(Integer roleId);

    /**
     * 获取用户的单位范围列表（下拉框用、KV）
     */
    List<TreeSelectVO> getUnitTreeSelectByUser(Integer userId);

    /**
     * 新增/更新单位
     */
    void upsertUnit(Unit unit);

    /**
     * 删除单位
     */
    void deleteUnit(Integer unitId);

    /**
     * 查询所有有简称单位列表（KV）
     */
    List<SelectVO> getShortNameList();

    /**
     * 查询所有单位map(id-单位名称)
     */
    Map<Integer, String> getUnitMapByName();

    /**
     * 查询所有单位map(简称-id)
     */
    Map<String, Integer> getUnitMapByAbbr();

    /**
     * 获取单位详情
     */
    Unit getUnitInfo(Integer unitId);

    /**
     * 获取指定单位的所有下级单位id
     */
    List<Integer> getSubUnit(Integer unitId);

    /**
     * 递归获取指定单位下（包含自己）的所有子单位id
     */
    List<Integer> getSubUnitByDeep(Integer unitId);
}
