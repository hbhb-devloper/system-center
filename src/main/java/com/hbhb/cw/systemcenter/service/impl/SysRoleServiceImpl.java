package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.common.constant.SecurityConstant;
import com.hbhb.cw.systemcenter.enums.RoleType;
import com.hbhb.cw.systemcenter.mapper.SysRoleMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserRoleMapper;
import com.hbhb.cw.systemcenter.model.SysRole;
import com.hbhb.cw.systemcenter.model.SysUserRole;
import com.hbhb.cw.systemcenter.service.SysRoleService;

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
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<Integer> getRolesByUserId(Integer userId) {
        List<SysUserRole> list = sysUserRoleMapper.createLambdaQuery()
                .andEq(SysUserRole::getUserId, userId)
                .select();
        return Optional.ofNullable(list)
                .orElse(new ArrayList<>())
                .stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAdminRole(Integer userId) {
        // 查询用户的所有RS角色
        List<SysRole> roles = sysRoleMapper.selectUserRolesByType(userId, RoleType.RELATE_RESOURCE.value());
        List<String> roleKeys = roles.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
        return roleKeys.contains(SecurityConstant.SUPER_ADMIN.value());
    }
}
