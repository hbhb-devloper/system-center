package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.cw.systemcenter.model.Role;
import com.hbhb.cw.systemcenter.service.RoleService;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;
import com.hbhb.cw.systemcenter.web.vo.RoleVO;

import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaokang
 * @since 2020-11-28
 */
@Tag(name = "角色")
@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {

    @Resource
    private RoleService roleService;

    @Operation(summary = "通过指定条件查询角色模板列表（分页）")
    @GetMapping("/page")
    public PageResult<RoleVO> pageRoleByCond(
            @Parameter(description = "页码") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "角色类型（RS-关联资源、UN-关联单位）", required = true) @RequestParam String roleType,
            @Parameter(description = "角色名称") @RequestParam(required = false) String roleName,
            @Parameter(description = "角色状态") @RequestParam(required = false) Byte state) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return roleService.getRolePageByCond(pageNum, pageSize,
                RoleVO.builder()
                        .roleType(roleType)
                        .roleName(roleName)
                        .state(state)
                        .build());
    }

    @Operation(summary = "获取指定类型的所有角色", description = "选择时用")
    @GetMapping("/list")
    public List<TreeSelectVO> getRoleList(
            @Parameter(description = "角色类型（RS-关联资源、UN-关联单位）", required = true) @RequestParam String roleType) {
        return roleService.getRoleListByType(roleType);
    }

    @Operation(summary = "获取用户的角色列表", description = "获取用户id对应的角色")
    @GetMapping("/user/{userId}")
    public List<Integer> getUserRoleList(
            @Parameter(description = "用户id", required = true) @PathVariable Integer userId,
            @Parameter(description = "角色/模板类型（RS-关联资源、UN-关联单位）", required = true) @RequestParam String roleType) {
        return roleService.getCheckedRoleByUser(userId, roleType);
    }

    @Operation(summary = "获取角色模板详情")
    @GetMapping("/{roleId}")
    public Role getRoleInfo(
            @Parameter(description = "角色id", required = true) @PathVariable Integer roleId) {
        return roleService.getRoleInfo(roleId);
    }

    @Operation(summary = "添加角色模板")
    @PostMapping("")
    public void addRole(@Parameter(description = "角色信息", required = true) @RequestBody Role role) {
        roleService.addRole(role);
    }

    @Operation(summary = "更新角色模板")
    @PutMapping("")
    public void updateRole(@Parameter(description = "角色信息", required = true) @RequestBody Role role) {
        role.setUpdateTime(new Date());
        roleService.updateRole(role);
    }

    @Operation(summary = "更新角色状态")
    @PutMapping("/{roleId}/state")
    public void changeRoleState(
            @Parameter(description = "角色id", required = true) @PathVariable Integer roleId,
            @Parameter(description = "状态值（0-删除、1-正常、2-停用）", required = true) @RequestParam Byte state) {
        roleService.changeState(roleId, state);
    }
}
