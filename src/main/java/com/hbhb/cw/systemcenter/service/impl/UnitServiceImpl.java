package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.bean.SelectVO;
import com.hbhb.core.utils.TreeUtil;
import com.hbhb.cw.systemcenter.enums.UnitEnum;
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
                .select(Unit::getId, Unit::getUnitName, Unit::getParentId);
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
        List<Unit> treeList = new ArrayList<>();
        // 以杭州和本部为根节点，组件树
        List<Unit> treeList1 = TreeUtil.build(units, UnitEnum.HANGZHOU.value().toString());
        List<Unit> treeList2 = TreeUtil.build(units, UnitEnum.BENBU.value().toString());
        treeList.addAll(treeList1);
        treeList.addAll(treeList2);
        // 转kv
        return treeList.stream()
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
    public List<Integer> getAllUnitId() {
        // 获取所有单位数据（按parentId升序）
        List<Unit> units = unitMapper.createLambdaQuery()
                .asc(Unit::getParentId)
                .asc(Unit::getSortNum)
                .select(Unit::getId);
        return Optional.ofNullable(units)
                .orElse(new ArrayList<>())
                .stream()
                .map(Unit::getId)
                // 过滤杭州和本部
                .filter(unitId -> !UnitEnum.isHangzhou(unitId) && !UnitEnum.isBenbu(unitId))
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
        List<Integer> ids = new ArrayList<>();
        ids.add(unitId);
        // 判断是否有下级
        // 如果没有下级，则直接返回；如果有下级，则先进行树形转换，再取出id。
        long count = unitMapper.createLambdaQuery().andEq(Unit::getParentId, unitId).count();
        if (count > 0) {
            List<Unit> units = unitMapper.createLambdaQuery().select(Unit::getId, Unit::getParentId);
            // 以 unitId 作为 rootId 构建树形结构（便于递归获取下属单位）
            List<Unit> treeList = TreeUtil.build(units, unitId.toString());
            // 取出单位id
            getIdFromTree(ids, treeList);
        }
        return ids;
    }

    /**
     * 递归从树形结构中取出id
     */
    private void getIdFromTree(List<Integer> ids, List<Unit> units) {
        units.forEach(unit -> {
            ids.add(unit.getId());
            if (!CollectionUtils.isEmpty(unit.getChildren())) {
                getIdFromTree(ids, unit.getChildren());
            }
        });
    }
}
