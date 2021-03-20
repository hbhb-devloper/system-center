package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.mapper.MaintainMapper;
import com.hbhb.cw.systemcenter.model.SysMaintain;
import com.hbhb.cw.systemcenter.service.SysMaintainService;
import com.hbhb.cw.systemcenter.service.SysRoleService;
import com.hbhb.cw.systemcenter.web.vo.MaintainVO;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SysMaintainServiceImpl implements SysMaintainService {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private MaintainMapper maintainMapper;

    @Override
    public MaintainVO getMaintainInfo() {
        MaintainVO vo = new MaintainVO();
        // 系统维护信息默认只有一条且id为1
        SysMaintain maintain = maintainMapper.single(1L);
        BeanUtils.copyProperties(maintain, vo);
        return vo;
    }

    @Override
    public void updateMaintain(MaintainVO vo, Integer userId) {
        SysMaintain maintain = new SysMaintain();
        BeanUtils.copyProperties(vo, maintain);
        // 系统维护信息默认只有一条且id为1
        maintain.setId(1L);
        // 判断登录用户是否为管理员，只有管理员才可修改系统维护信息
        boolean admin = sysRoleService.isAdminRole(userId);
        if (admin) {
            maintainMapper.updateTemplateById(maintain);
        }
    }
}
