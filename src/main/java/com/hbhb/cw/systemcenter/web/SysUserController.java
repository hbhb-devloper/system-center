package com.hbhb.cw.systemcenter.web;

import com.hbhb.cw.systemcenter.api.SysUserApi;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.service.SysResourceService;
import com.hbhb.cw.systemcenter.service.SysRoleService;
import com.hbhb.cw.systemcenter.service.SysUserService;
import com.hbhb.cw.systemcenter.vo.SysUserInfo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Tag(name = "用户相关")
@RestController
@RequestMapping("/user")
@Slf4j
public class SysUserController implements SysUserApi {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysResourceService sysResourceService;

    @Override
    public SysUserInfo getUserById(Integer userId) {
        return sysUserService.getUserById(userId);
    }

    @Override
    public SysUser getUserByName(String userName) {
        return sysUserService.getUserByName(userName);
    }

    @Override
    public List<Integer> getUserRoles(Integer userId) {
        return sysRoleService.getRolesByUserId(userId);
    }

    @Override
    public List<String> getUserPerms(Integer userId) {
        return sysResourceService.getPermsByUserId(userId);
    }
}