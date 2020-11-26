package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.mapper.UserMapper;
import com.hbhb.cw.systemcenter.model.User;
import com.hbhb.cw.systemcenter.service.ResourceService;
import com.hbhb.cw.systemcenter.service.UserService;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.vo.UserVO;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ResourceService resourceService;

    @Override
    public UserInfo getUserById(Integer userId) {
        User user = userMapper.single(userId);
        List<String> perms = resourceService.getPermsByUserId(userId);
        return UserInfo.builder()
                .id(userId)
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .unitId(user.getUnitId())
                .perms(perms)
                .build();
    }

    @Override
    public User getUserByName(String username) {
        return userMapper.createLambdaQuery()
                .andEq(User::getUserName, username)
                .single();
    }

    @Override
    public List<UserVO> getUserList(List<Integer> userIds) {
        List<User> sysUsers = userMapper.selectByIds(userIds);
        List<UserVO> userVOList = new ArrayList<>();
        for (User item : sysUsers) {
            UserVO vo = UserVO.builder()
                    .email(item.getEmail())
                    .nickName(item.getNickName())
                    .id(item.getId())
                    .unitId(item.getUnitId())
                    .build();
            userVOList.add(vo);
        }
        return userVOList;
    }
}
