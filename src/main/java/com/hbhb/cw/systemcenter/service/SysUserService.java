package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.SysUser;

import java.util.Set;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface SysUserService {

    /**
     * 根据登录名获取用户信息
     */
    SysUser getUserByUsername(String username);

    /**
     * 获取用户的权限集合
     */
    Set<String> getUserAllPerms(Integer userId);
}
