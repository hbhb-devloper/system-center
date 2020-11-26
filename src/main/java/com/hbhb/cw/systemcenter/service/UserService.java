package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.User;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.vo.UserVO;

import java.util.List;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface UserService {

    /**
     * 根据id获取用户信息
     */
    UserInfo getUserById(Integer userId);

    /**
     * 根据登录名获取用户信息
     */
    User getUserByName(String username);

    /**
     * 跟据用户id列表获取用户信息列表
     *
     * @param userIds 用户id列表
     * @return 用户信息列表
     */
    List<UserVO> getUserList(List<Integer> userIds);
}
