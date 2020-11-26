package com.hbhb.cw.systemcenter.service;

import java.util.List;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface RoleService {

    /**
     * 获取用户的所有角色id
     */
    List<Integer> getRolesByUserId(Integer userId);

    /**
     * 判断某用户是否为管理员角色
     */
    boolean isAdminRole(Integer userId);
}
