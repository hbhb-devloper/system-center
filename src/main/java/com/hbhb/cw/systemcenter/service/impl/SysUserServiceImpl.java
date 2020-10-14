package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.mapper.SysUserMapper;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.service.SysResourceService;
import com.hbhb.cw.systemcenter.service.SysUserService;
import com.hbhb.cw.systemcenter.vo.SysUserInfo;

import org.springframework.stereotype.Service;

import java.util.List;

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
    private SysResourceService sysResourceService;

    @Override
    public SysUserInfo getUserById(Integer userId) {
        SysUser sysUser = sysUserMapper.single(userId);
        List<String> perms = sysResourceService.getPermsByUserId(userId);
        return SysUserInfo.builder()
                .id(userId)
                .userName(sysUser.getUserName())
                .nickName(sysUser.getNickName())
                .perms(perms)
                .build();
    }

    @Override
    public SysUser getUserByName(String username) {
        return sysUserMapper.createLambdaQuery()
                .andEq(SysUser::getUserName, username)
                .single();
    }

}
