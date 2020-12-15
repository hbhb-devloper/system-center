package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.core.bean.BeanConverter;
import com.hbhb.core.bean.SelectVO;
import com.hbhb.cw.systemcenter.api.UnitApi;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.model.Unit;
import com.hbhb.cw.systemcenter.service.SysUserService;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;
import com.hbhb.cw.systemcenter.vo.TreeSelectWrapVO;
import com.hbhb.cw.systemcenter.web.vo.SelectInputVO;
import com.hbhb.web.annotation.UserId;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "单位")
@RestController
@RequestMapping("/unit")
@Slf4j
public class UnitController implements UnitApi {

    @Resource
    private UnitService unitService;
    @Resource
    private SysUserService sysUserService;

    @Operation(summary = "获取所有的单位列表", description = "单位管理时用，树形结构")
    @GetMapping("/list")
    public List<TreeSelectVO> getAllUnitList() {
        return unitService.getAllUnitTreeList();
    }

    @Operation(summary = "获取角色的单位列表", description = "获取角色id对应的单位列表")
    @GetMapping("/role/{roleId}")
    public List<Integer> getRoleResourceTreeSelect(
            @Parameter(description = "角色id", required = true) @PathVariable Integer roleId) {
        return unitService.getCheckedUnitByRole(roleId);
    }

    @Operation(summary = "获取用户的单位列表树", description = "根据用户id获取可用的单位列表，下拉框选择时用，树形结构")
    @GetMapping("/tree-select")
    public TreeSelectWrapVO getUnitTreeSelect(@Parameter(hidden = true) @UserId Integer userId) {
        // 每次需要从数据库拿最新的 defaultUnitId
        SysUser user = sysUserService.getUserById(userId);
        // 用户的单位范围列表
        List<TreeSelectVO> list = unitService.getUnitTreeSelectByUser(userId);
        return TreeSelectWrapVO.builder()
                .checked(Collections.singletonList(user.getDefaultUnitId()))
                .list(list).build();
    }

    @Operation(summary = "添加单位")
    @PostMapping("")
    public void addUnit(@Parameter(description = "单位信息", required = true) @RequestBody Unit unit) {
        unitService.upsertUnit(unit);
    }

    @Operation(summary = "更新单位")
    @PutMapping("")
    public void updateUnit(@Parameter(description = "单位信息", required = true) @RequestBody Unit unit) {
        unit.setUpdateTime(new Date());
        unitService.upsertUnit(unit);
    }

    @Operation(summary = "删除单位")
    @DeleteMapping("/{unitId}")
    public void deleteUnit(@Parameter(description = "单位id", required = true) @PathVariable Integer unitId) {
        unitService.deleteUnit(unitId);
    }

    @Operation(summary = "获取有input框的单位id和简称")
    @GetMapping("/short-name/list")
    public List<SelectInputVO> getShortName() {
        List<SelectVO> shortNameList = unitService.getShortNameList();
        return BeanConverter.copyBeanList(shortNameList, SelectInputVO.class);
    }

    @Operation(summary = "获取所有单位id", description = "不包含杭州和本部")
    @Override
    public List<Integer> getAllUnitId() {
        return unitService.getAllUnitId();
    }

    @Operation(summary = "获取单位map", description = "单位id-单位名称")
    @Override
    public Map<Integer, String> getUnitMapById() {
        return unitService.getUnitMapById();
    }

    @Operation(summary = "获取单位map", description = "单位名称-单位id")
    @Override
    public Map<String, Integer> getUnitMapByUnitName() {
        return unitService.getUnitMapByUnitName();
    }

    @Operation(summary = "获取单位map", description = "单位缩写名-单位id")
    @Override
    public Map<String, Integer> getUnitMapByShortName() {
        return unitService.getUnitMapByShortName();
    }

    @Operation(summary = "获取单位map", description = "单位简称-单位id")
    @Override
    public Map<String, Integer> getUnitMapByAbbr() {
        return unitService.getUnitMapByAbbr();
    }

    @Operation(summary = "获取单位详情")
    @Override
    public Unit getUnitInfo(@Parameter(description = "单位id", required = true) Integer unitId) {
        return unitService.getUnitInfo(unitId);
    }

    @Operation(summary = "获取某单位的所有下级单位id（递归遍历，包含自身）")
    @Override
    public List<Integer> getSubUnit(@Parameter(description = "单位id", required = true) Integer unitId) {
        return unitService.getSubUnit(unitId);
    }
}
