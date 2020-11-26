package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.constants.AuthConstant;
import com.hbhb.cw.systemcenter.enums.RoleType;
import com.hbhb.cw.systemcenter.mapper.RoleMapper;
import com.hbhb.cw.systemcenter.mapper.UserRoleMapper;
import com.hbhb.cw.systemcenter.model.Role;
import com.hbhb.cw.systemcenter.model.UserRole;
import com.hbhb.cw.systemcenter.service.RoleService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Integer> getRolesByUserId(Integer userId) {
        List<UserRole> list = userRoleMapper.createLambdaQuery()
                .andEq(UserRole::getUserId, userId)
                .select();
        return Optional.ofNullable(list)
                .orElse(new ArrayList<>())
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAdminRole(Integer userId) {
        // 查询用户的所有RS角色
        List<Role> roles = roleMapper.selectUserRolesByType(userId, RoleType.RELATE_RESOURCE.value());
        List<String> roleKeys = roles.stream().map(Role::getRoleKey).collect(Collectors.toList());
        return roleKeys.contains(AuthConstant.SUPER_ADMIN.value());
    }
}
