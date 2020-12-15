package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.constants.AuthConstant;
import com.hbhb.cw.systemcenter.enums.RoleType;
import com.hbhb.cw.systemcenter.mapper.SysRoleMapper;
import com.hbhb.cw.systemcenter.mapper.SysRoleResourceMapper;
import com.hbhb.cw.systemcenter.mapper.SysRoleUnitMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserRoleMapper;
import com.hbhb.cw.systemcenter.model.SysRole;
import com.hbhb.cw.systemcenter.model.SysRoleResource;
import com.hbhb.cw.systemcenter.model.SysRoleUnit;
import com.hbhb.cw.systemcenter.model.SysUserRole;
import com.hbhb.cw.systemcenter.service.SysRoleService;
import com.hbhb.cw.systemcenter.vo.CheckBoxVO;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;
import com.hbhb.cw.systemcenter.web.vo.RoleVO;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRoleUnitMapper sysRoleUnitMapper;
    @Resource
    private SysRoleResourceMapper sysRoleResourceMapper;

    @Override
    @SuppressWarnings(value = {"rawtypes"})
    public PageResult<RoleVO> getRolePageByCond(Integer pageNum, Integer pageSize, RoleVO vo) {
        PageRequest request = DefaultPageRequest.of(pageNum, pageSize);
        return sysRoleMapper.selectPageByCond(vo, request);
    }

    @Override
    public List<TreeSelectVO> getRoleListByType(String roleType) {
        List<SysRole> roles = sysRoleMapper.selectUserRolesByType(null, roleType);
        return Optional.ofNullable(roles)
                .orElse(new ArrayList<>())
                .stream()
                .map(TreeSelectVO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getCheckedRoleByUser(Integer userId, String roleType) {
        List<SysRole> roles = sysRoleMapper.selectUserRolesByType(userId, roleType);
        return Optional.ofNullable(roles)
                .orElse(new ArrayList<>())
                .stream()
                .map(SysRole::getId)
                .collect(Collectors.toList());
    }

    @Override
    public SysRole getRoleInfo(Integer roleId) {
        return sysRoleMapper.single(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(SysRole role) {
        // 先添加主表
        sysRoleMapper.insertTemplate(role);
        // 再添加关联表
        String roleType = role.getRoleType();
        if (roleType.equals(RoleType.RELATE_RESOURCE.value())) {
            insertRoleResource(role);
        } else if (roleType.equals(RoleType.RELATE_UNIT.value())) {
            insertRoleUnit(role);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRole role) {
        Integer roleId = role.getId();
        // 先修改主表
        sysRoleMapper.updateTemplateById(role);
        // 再删除对应关联表，最后添加新的关联数据
        String roleType = role.getRoleType();
        if (roleType.equals(RoleType.RELATE_RESOURCE.value())) {
            sysRoleResourceMapper.createLambdaQuery()
                    .andEq(SysRoleResource::getRoleId, roleId)
                    .delete();
            insertRoleResource(role);
        } else if (roleType.equals(RoleType.RELATE_UNIT.value())) {
            sysRoleUnitMapper.createLambdaQuery()
                    .andEq(SysRoleUnit::getRoleId, roleId)
                    .delete();
            insertRoleUnit(role);
        }
    }


    @Override
    public void changeState(Integer roleId, Byte state) {
        sysRoleMapper.updateTemplateById(SysRole.builder()
                .id(roleId)
                .state(state).build());
    }

    @Override
    public List<Integer> getRolesByUserId(Integer userId) {
        List<SysUserRole> list = sysUserRoleMapper.createLambdaQuery()
                .andEq(SysUserRole::getUserId, userId)
                .select();
        return Optional.ofNullable(list)
                .orElse(new ArrayList<>())
                .stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAdminRole(Integer userId) {
        // 查询用户的所有RS角色
        List<SysRole> roles = sysRoleMapper.selectUserRolesByType(userId, RoleType.RELATE_RESOURCE.value());
        List<String> roleKeys = roles.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
        return roleKeys.contains(AuthConstant.ADMIN.value());
    }

    /**
     * 添加角色资源关联表
     */
    private void insertRoleResource(SysRole role) {
        List<CheckBoxVO> resourceIds = role.getCheckedResourceIds();
        if (StringUtils.isEmpty(resourceIds)) {
            return;
        }
        List<SysRoleResource> list = new ArrayList<>();
        for (CheckBoxVO vo : resourceIds) {
            list.add(SysRoleResource.builder()
                    .roleId(role.getId())
                    .resourceId(vo.getId())
                    .isHalf(vo.getIsHalf())
                    .build());
        }
        if (!CollectionUtils.isEmpty(list)) {
            sysRoleResourceMapper.insertBatch(list);
        }
    }

    /**
     * 添加角色单位关联表
     */
    private void insertRoleUnit(SysRole role) {
        List<CheckBoxVO> unitIds = role.getCheckedUnitIds();
        if (StringUtils.isEmpty(unitIds)) {
            return;
        }
        List<SysRoleUnit> list = new ArrayList<>();
        for (CheckBoxVO vo : unitIds) {
            list.add(SysRoleUnit.builder()
                    .roleId(role.getId())
                    .unitId(vo.getId())
                    .isHalf(vo.getIsHalf())
                    .build());
        }
        if (!CollectionUtils.isEmpty(list)) {
            sysRoleUnitMapper.insertBatch(list);
        }
    }
}
