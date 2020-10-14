package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.vo.SysUserInfo;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface SysUserService {

    /**
     * 根据id获取用户信息
     */
    SysUserInfo getUserById(Integer userId);

    /**
     * 根据登录名获取用户信息
     */
    SysUser getUserByName(String username);

}
