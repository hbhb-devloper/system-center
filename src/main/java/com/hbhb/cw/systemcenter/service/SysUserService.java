package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.vo.SysUserInfo;
import com.hbhb.cw.systemcenter.vo.SysUserVO;

import java.util.List;

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

    /**
     * 跟据用户id列表获取用户信息列表
     * @param userIds 用户id列表
     * @return 用户信息列表
     */
    List<SysUserVO> getUserList(List<Integer> userIds);
}
