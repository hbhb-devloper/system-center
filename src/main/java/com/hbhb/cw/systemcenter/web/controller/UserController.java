package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.cw.systemcenter.api.UserApi;
import com.hbhb.cw.systemcenter.model.User;
import com.hbhb.cw.systemcenter.service.ResourceService;
import com.hbhb.cw.systemcenter.service.RoleService;
import com.hbhb.cw.systemcenter.service.UserService;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.vo.UserVO;

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
public class UserController implements UserApi {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private ResourceService resourceService;

    @Operation(summary = "根据用户id获取用户的基本信息")
    @Override
    public UserInfo getUserById(@Parameter(description = "用户id", required = true) Integer userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "根据登录名获取用户详情")
    @Override
    public User getUserByName(@Parameter(description = "登录名", required = true) String userName) {
        return userService.getUserByName(userName);
    }

    @Operation(summary = "获取用户所有角色")
    @Override
    public List<Integer> getUserRoles(@Parameter(description = "用户id", required = true) Integer userId) {
        return roleService.getRolesByUserId(userId);
    }

    @Operation(summary = "获取用户所有权限")
    @Override
    public List<String> getUserPerms(@Parameter(description = "用户id", required = true) Integer userId) {
        return resourceService.getPermsByUserId(userId);
    }

    @Override
    public List<UserVO> getUserList(List<Integer> userIds) {
        return userService.getUserList(userIds);
    }
}
