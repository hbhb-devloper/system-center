package com.hbhb.cw.systemcenter.service;

import com.hbhb.api.core.bean.SelectVO;
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
     * 获取杭州直属分公司列表
     */
    List<SelectVO> getSubordinateList();

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
     * 获取所有单位id（不包含杭州和本部）
     */
    List<Integer> getAllUnitId();

    /**
     * 查询所有单位map(id-单位名称)
     */
    Map<Integer, String> getUnitMapById();

    /**
     * 查询所有单位map(单位名称-id)
     */
    Map<String, Integer> getUnitMapByUnitName();

    /**
     * 查询所有单位map(单位缩写名-id)
     */
    Map<String, Integer> getUnitMapByShortName();

    /**
     * 查询所有单位map(简称-id)
     */
    Map<String, Integer> getUnitMapByAbbr();

    /**
     * 获取单位详情
     */
    Unit getUnitInfo(Integer unitId);

    /**
     * 获取单位名称
     */
    String getUnitName(Integer unitId);

    /**
     * 递归获取指定单位下的所有下属单位id（包含自己）
     */
    List<Integer> getSubUnit(Integer unitId);
}
