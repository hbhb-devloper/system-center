package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.core.utils.TreeUtil;
import com.hbhb.cw.systemcenter.enums.UnitEnum;
import com.hbhb.cw.systemcenter.mapper.SysRoleUnitMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserUintHallMapper;
import com.hbhb.cw.systemcenter.mapper.UnitMapper;
import com.hbhb.cw.systemcenter.model.SysRoleUnit;
import com.hbhb.cw.systemcenter.model.SysUserUintHall;
import com.hbhb.cw.systemcenter.model.Unit;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
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
    private SysRoleUnitMapper sysRoleUnitMapper;
    @Resource
    private SysUserUintHallMapper sysUserUintHallMapper;

    @Override
    public List<TreeSelectVO> getAllUnitTreeList() {
        // 查询所有单位列表（按parentId、显示顺序升序）
        List<Unit> units = unitMapper.createLambdaQuery()
                .asc(Unit::getParentId).asc(Unit::getSortNum)
                .select(Unit::getId, Unit::getUnitName, Unit::getParentId);
        // 以杭州的parentId=0为根节点，进行树形转换
        List<Unit> treeList = TreeUtil.build(units, "0");
        // 转kv
        return Optional.ofNullable(treeList)
                .orElse(new ArrayList<>())
                .stream()
                .map(TreeSelectVO::new).collect(Collectors.toList());
    }

    @Override
    public List<SelectVO> getSubordinateList() {
        List<Unit> list = unitMapper.createLambdaQuery()
                .andEq(Unit::getParentId, UnitEnum.HANGZHOU.value())
                .select(Unit::getId, Unit::getUnitName);
        return Optional.ofNullable(list)
                .orElse(new ArrayList<>())
                .stream()
                .map(unit -> SelectVO.builder()
                        .id(Long.valueOf(unit.getId()))
                        .label(unit.getUnitName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getCheckedUnitByRole(Integer roleId) {
        List<SysRoleUnit> list = sysRoleUnitMapper.createLambdaQuery()
                .andEq(SysRoleUnit::getRoleId, roleId)
                .select();
        return list.stream().map(SysRoleUnit::getUnitId).collect(Collectors.toList());
    }

    @Override
    public List<TreeSelectVO> getUnitTreeSelectByUser(Integer userId) {
        // 查询用户的单位列表（按parentId、显示顺序升序，并且已去掉半选节点）current
        List<Integer> unitIds =  sysUserUintHallMapper.createLambdaQuery().andEq(SysUserUintHall::getUserId,userId)
                .select().stream().map(SysUserUintHall::getUintId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        List<Unit> units =  unitMapper.createLambdaQuery().andIn(Unit::getId,unitIds).asc(Unit::getParentId).asc(Unit::getSortNum).select();

        if (CollectionUtils.isEmpty(units)) {
            return new ArrayList<>();
        }

        // 将单位列表转成树形kv结构
        List<Unit> treeList = new ArrayList<>();

        if (unitIds.contains(UnitEnum.HANGZHOU.value())) {
            // 如果包含杭州，则以0为root节点构建树
            treeList.addAll(TreeUtil.build(units, "0"));
        } else if (unitIds.contains(UnitEnum.BENBU.value())) {
            // 如果包含本部，则以杭州id为root节点构建树
            treeList.addAll(TreeUtil.build(units, UnitEnum.HANGZHOU.value().toString()));
        }else {
            // 既不包含杭州，也不包含本部，则直接返回
            treeList.addAll(units);
        }

        // 转kv
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
    public String getUnitName(Integer unitId) {
        Unit unit = unitMapper.single(unitId);
        return unit == null ? "" : unit.getUnitName();
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
