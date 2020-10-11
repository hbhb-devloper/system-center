package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.common.constant.SecurityConstant;
import com.hbhb.cw.systemcenter.enums.ResourceType;
import com.hbhb.cw.systemcenter.mapper.SysResourceMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserMapper;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.service.SysRoleService;
import com.hbhb.cw.systemcenter.service.SysUserService;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysResourceMapper sysResourceMapper;
    @Resource
    private SysRoleService sysRoleService;

    @Override
    public SysUser getUserByUsername(String username) {
        return sysUserMapper.createLambdaQuery()
                .andEq(SysUser::getUserName, username)
                .single();
    }

    @Override
    public Set<String> getUserAllPerms(Integer userId) {
        Set<String> perms = new HashSet<>();
        // 判断用户角色是否为管理员
        boolean isAdmin = sysRoleService.isAdminRole(userId);
        if (isAdmin) {
            // 管理员拥有系统的全部权限
            perms.add(SecurityConstant.ALL_PERMISSION.value());
        } else {
            perms.addAll(sysResourceMapper.selectUserPermsByType(userId, ResourceType.getAll()));
        }
        return perms;
    }
}
