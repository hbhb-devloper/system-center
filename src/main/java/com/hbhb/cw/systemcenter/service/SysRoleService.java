package com.hbhb.cw.systemcenter.service;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface SysRoleService {

    /**
     * 判断某用户是否为管理员角色
     */
    boolean isAdminRole(Integer userId);
}
