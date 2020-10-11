package com.hbhb.cw.systemcenter.web;

import com.hbhb.cw.systemcenter.api.SysUserApi;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.service.SysUserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import javax.annotation.Resource;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Api(tags = "用户相关")
@RestController
@RequestMapping("/user")
@Slf4j
public class SysUserController implements SysUserApi {

    @Resource
    private SysUserService sysUserService;

    @Override
    public SysUser getUserByName(String userName) {
        return sysUserService.getUserByUsername(userName);
    }

    @Override
    public Set<String> getUserAllPerms(Integer userId) {
        return sysUserService.getUserAllPerms(userId);
    }
}
