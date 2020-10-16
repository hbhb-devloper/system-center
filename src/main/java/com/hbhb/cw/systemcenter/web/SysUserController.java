package com.hbhb.cw.systemcenter.web;

import com.hbhb.cw.systemcenter.api.SysUserApi;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.service.SysResourceService;
import com.hbhb.cw.systemcenter.service.SysRoleService;
import com.hbhb.cw.systemcenter.service.SysUserService;
import com.hbhb.cw.systemcenter.vo.SysUserInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Tag(name = "用户相关")
@RestController
@RequestMapping("/user")
@Slf4j
public class SysUserController implements SysUserApi {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysResourceService sysResourceService;

    @Operation(summary = "根据用户id获取用户的基本信息")
    @Override
    public SysUserInfo getUserById(@Parameter(description = "用户id", required = true) Integer userId) {
        return sysUserService.getUserById(userId);
    }

    @Operation(summary = "根据登录名获取用户详情")
    @Override
    public SysUser getUserByName(@Parameter(description = "登录名", required = true) String userName) {
        return sysUserService.getUserByName(userName);
    }

    @Operation(summary = "获取用户所有角色")
    @Override
    public List<Integer> getUserRoles(@Parameter(description = "用户id", required = true) Integer userId) {
        return sysRoleService.getRolesByUserId(userId);
    }

    @Operation(summary = "获取用户所有权限")
    @Override
    public List<String> getUserPerms(@Parameter(description = "用户id", required = true) Integer userId) {
        return sysResourceService.getPermsByUserId(userId);
    }
}
