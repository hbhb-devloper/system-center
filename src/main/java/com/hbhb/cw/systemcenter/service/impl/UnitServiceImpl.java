package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.bean.SelectVO;
import com.hbhb.core.utils.TreeUtil;
import com.hbhb.cw.systemcenter.mapper.RoleUnitMapper;
import com.hbhb.cw.systemcenter.mapper.UnitMapper;
import com.hbhb.cw.systemcenter.model.RoleUnit;
import com.hbhb.cw.systemcenter.model.Unit;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UnitServiceImpl implements UnitService {

    @Resource
    private UnitMapper unitMapper;
    @Resource
    private RoleUnitMapper roleUnitMapper;

    @Override
    public List<TreeSelectVO> getAllUnitTreeList() {
        // 获取所有单位数据（按parentId升序）
        List<Unit> units = unitMapper.createLambdaQuery()
                .asc(Unit::getParentId)
                .asc(Unit::getSortNum)
                .select();
        if (CollectionUtils.isEmpty(units)) {
            return new ArrayList<>();
        }

        // 以第一条数据的 parentId 作为 rootId 进行树形构建
        Integer rootId = units.get(0).getParentId();
        List<Unit> treeList = TreeUtil.build(units, rootId.toString());
        if (CollectionUtils.isEmpty(treeList)) {
            return new ArrayList<>();
        }
        return treeList.stream().map(TreeSelectVO::new).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getCheckedUnitByRole(Integer roleId) {
        List<RoleUnit> list = roleUnitMapper.createLambdaQuery()
                .andEq(RoleUnit::getRoleId, roleId)
                .select();
        return list.stream().map(RoleUnit::getUnitId).collect(Collectors.toList());
    }

    @Override
    public List<TreeSelectVO> getUnitTreeSelectByUser(Integer userId) {
        // 查询用户的单位列表（按parentId升序）
        List<Unit> units = unitMapper.selectByUserId(userId);
        if (CollectionUtils.isEmpty(units)) {
            return new ArrayList<>();
        }
        // 转换成树形结构
        // 以第一条数据的 parentId 作为 rootId 进行树形构建
        Integer rootId = units.get(0).getParentId();
        List<Unit> treeList = TreeUtil.build(units, rootId.toString());
        if (CollectionUtils.isEmpty(treeList)) {
            return new ArrayList<>();
        }
        // 转KV
        return treeList.stream().map(TreeSelectVO::new).collect(Collectors.toList());
    }

    @Override
    public void upsertUnit(Unit unit) {
        unitMapper.upsertByTemplate(unit);
    }

    @Override
    public void deleteUnit(Integer unitId) {
        unitMapper.deleteById(unitId);
    }

    @Override
    public List<Integer> getUnitSubsByUnitId(Integer unitId) {
        List<Integer> ids = new ArrayList<>();
        // 包含自己单位id
        ids.add(unitId);

        // 获取所有单位数据（按parentId升序）
        List<Unit> units = unitMapper.all();
        if (CollectionUtils.isEmpty(units)) {
            return ids;
        }

        // 以 unitId 作为 rootId 进行树形构建（进行筛选）
        List<Unit> treeList = TreeUtil.build(units, unitId.toString());
        if (CollectionUtils.isEmpty(treeList)) {
            return ids;
        }

        // 取出单位id
        getUnitIdsFromTree(ids, treeList);
        return ids;
    }

    /**
     * 从树形结构中取出id
     */
    private void getUnitIdsFromTree(List<Integer> ids, List<Unit> units) {
        units.forEach(unit -> {
            ids.add(unit.getId());
            if (!CollectionUtils.isEmpty(unit.getChildren())) {
                getUnitIdsFromTree(ids, unit.getChildren());
            }
        });
    }

    @Override
    public List<SelectVO> getShortNameList() {
        List<SelectVO> list = new ArrayList<>();
        List<Unit> units = unitMapper.all();
        if (CollectionUtils.isEmpty(units)) {
            return list;
        }
        units.forEach(unit -> {
            if (!StringUtils.isEmpty(unit.getShortName())) {
                list.add(SelectVO.builder()
                        .id((long) unit.getId())
                        .label(unit.getShortName())
                        .build());
            }
        });
        return list;
    }

    @Override
    public List<Unit> getAllUnitList() {
        return unitMapper.all();
    }

    @Override
    public Unit getUnitInfo(Integer unitId) {
        return unitMapper.single(unitId);
    }

    @Override
    public List<Integer> getSubUnitId(Integer unitId) {
        List<Unit> subUnits = unitMapper.createLambdaQuery()
                .andEq(Unit::getParentId, unitId)
                .select();
        if (CollectionUtils.isEmpty(subUnits)) {
            return new ArrayList<>();
        }
        return subUnits.stream().map(Unit::getId).collect(Collectors.toList());
    }
}
