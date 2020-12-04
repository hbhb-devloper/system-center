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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@SuppressWarnings(value = {"unchecked"})
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
        return CollectionUtils.isEmpty(units) ? new ArrayList<>() : buildTreeSelectVO(units);
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
        return CollectionUtils.isEmpty(units) ? new ArrayList<>() : buildTreeSelectVO(units);
    }

    /**
     * 将单位列表转成树形kv结构
     */
    private List<TreeSelectVO> buildTreeSelectVO(List<Unit> units) {
        // 先转换成树形结构
        // 以第一条数据的 parentId 作为 rootId 进行树形构建
        Integer rootId = units.get(0).getParentId();
        List<Unit> treeList = TreeUtil.build(units, rootId.toString());

        // 再转k-v结构
        return Optional.ofNullable(treeList)
                .orElse(new ArrayList<>())
                .stream()
                .map(TreeSelectVO::new)
                .collect(Collectors.toList());
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
    public List<SelectVO> getShortNameList() {
        List<Unit> units = unitMapper.createLambdaQuery()
                .asc(Unit::getSortNum)
                .select(Unit::getId, Unit::getShortName);
        return Optional.ofNullable(units)
                .orElse(new ArrayList<>())
                .stream()
                .map(unit -> StringUtils.isEmpty(unit.getShortName()) ? null : SelectVO.builder()
                        .id((long) unit.getId())
                        .label(unit.getShortName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, String> getUnitMapById() {
        List<Unit> units = unitMapper.createLambdaQuery()
                .select(Unit::getId, Unit::getUnitName);
        return Optional.ofNullable(units)
                .orElse(new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(Unit::getId, Unit::getUnitName));
    }

    @Override
    public Map<String, Integer> getUnitMapByUnitName() {
        List<Unit> units = unitMapper.createLambdaQuery()
                .select(Unit::getId, Unit::getUnitName);
        return Optional.ofNullable(units)
                .orElse(new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(Unit::getUnitName, Unit::getId));
    }

    @Override
    public Map<String, Integer> getUnitMapByShortName() {
        List<Unit> units = unitMapper.createLambdaQuery()
                .select(Unit::getId, Unit::getShortName);
        return Optional.ofNullable(units)
                .orElse(new ArrayList<>())
                .stream()
                .filter(unit -> !StringUtils.isEmpty(unit.getShortName()))
                .collect(Collectors.toMap(Unit::getShortName, Unit::getId));
    }

    @Override
    public Map<String, Integer> getUnitMapByAbbr() {
        List<Unit> units = unitMapper.createLambdaQuery()
                .select(Unit::getAbbr, Unit::getId);
        return Optional.ofNullable(units)
                .orElse(new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(Unit::getAbbr, Unit::getId));
    }

    @Override
    public Unit getUnitInfo(Integer unitId) {
        return unitMapper.single(unitId);
    }

    @Override
    public List<Integer> getSubUnit(Integer unitId) {
        List<Unit> subUnits = unitMapper.createLambdaQuery()
                .andEq(Unit::getParentId, unitId)
                .select();
        return Optional.ofNullable(subUnits)
                .orElse(new ArrayList<>())
                .stream()
                .map(Unit::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getSubUnitByDeep(Integer unitId) {
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
     * 递归从树形结构中取出id
     */
    private void getUnitIdsFromTree(List<Integer> ids, List<Unit> units) {
        units.forEach(unit -> {
            ids.add(unit.getId());
            if (!CollectionUtils.isEmpty(unit.getChildren())) {
                getUnitIdsFromTree(ids, unit.getChildren());
            }
        });
    }
}
