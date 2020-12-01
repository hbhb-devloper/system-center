package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.constants.AuthConstant;
import com.hbhb.cw.systemcenter.enums.RoleType;
import com.hbhb.cw.systemcenter.mapper.RoleMapper;
import com.hbhb.cw.systemcenter.mapper.RoleResourceMapper;
import com.hbhb.cw.systemcenter.mapper.RoleUnitMapper;
import com.hbhb.cw.systemcenter.mapper.UserRoleMapper;
import com.hbhb.cw.systemcenter.model.Role;
import com.hbhb.cw.systemcenter.model.RoleResource;
import com.hbhb.cw.systemcenter.model.RoleUnit;
import com.hbhb.cw.systemcenter.model.UserRole;
import com.hbhb.cw.systemcenter.service.RoleService;
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
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleUnitMapper roleUnitMapper;
    @Resource
    private RoleResourceMapper roleResourceMapper;

    @Override
    @SuppressWarnings(value = {"rawtypes"})
    public PageResult<RoleVO> getRolePageByCond(Integer pageNum, Integer pageSize, RoleVO vo) {
        PageRequest request = DefaultPageRequest.of(pageNum, pageSize);
        return roleMapper.selectPageByCond(vo, request);
    }

    @Override
    public List<TreeSelectVO> getRoleListByType(String roleType) {
        List<Role> roles = roleMapper.selectUserRolesByType(null, roleType);
        if (CollectionUtils.isEmpty(roles)) {
            return new ArrayList<>();
        }
        return roles.stream().map(TreeSelectVO::new).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getCheckedRoleByUser(Integer userId, String roleType) {
        List<Role> roles = roleMapper.selectUserRolesByType(userId, roleType);
        if (CollectionUtils.isEmpty(roles)) {
            return new ArrayList<>();
        }
        return roles.stream().map(Role::getId).collect(Collectors.toList());
    }

    @Override
    public Role getRoleInfo(Integer roleId) {
        return roleMapper.single(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(Role role) {
        // 先添加主表
        roleMapper.insertTemplate(role);
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
    public void updateRole(Role role) {
        Integer roleId = role.getId();
        // 先修改主表
        roleMapper.updateTemplateById(role);
        // 再删除对应关联表，最后添加新的关联数据
        String roleType = role.getRoleType();
        if (roleType.equals(RoleType.RELATE_RESOURCE.value())) {
            roleResourceMapper.createLambdaQuery()
                    .andEq(RoleResource::getRoleId, roleId)
                    .delete();
            insertRoleResource(role);
        } else if (roleType.equals(RoleType.RELATE_UNIT.value())) {
            roleUnitMapper.createLambdaQuery()
                    .andEq(RoleUnit::getRoleId, roleId)
                    .delete();
            insertRoleUnit(role);
        }
    }


    @Override
    public void changeState(Integer roleId, Byte state) {
        roleMapper.updateTemplateById(Role.builder()
                .id(roleId)
                .state(state).build());
    }

    @Override
    public List<Integer> getRolesByUserId(Integer userId) {
        List<UserRole> list = userRoleMapper.createLambdaQuery()
                .andEq(UserRole::getUserId, userId)
                .select();
        return Optional.ofNullable(list)
                .orElse(new ArrayList<>())
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAdminRole(Integer userId) {
        // 查询用户的所有RS角色
        List<Role> roles = roleMapper.selectUserRolesByType(userId, RoleType.RELATE_RESOURCE.value());
        List<String> roleKeys = roles.stream().map(Role::getRoleKey).collect(Collectors.toList());
        return roleKeys.contains(AuthConstant.ADMIN.value());
    }

    /**
     * 添加角色资源关联表
     */
    private void insertRoleResource(Role role) {
        List<CheckBoxVO> resourceIds = role.getCheckedResourceIds();
        if (StringUtils.isEmpty(resourceIds)) {
            return;
        }
        List<RoleResource> list = new ArrayList<>();
        for (CheckBoxVO vo : resourceIds) {
            list.add(RoleResource.builder()
                    .roleId(role.getId())
                    .resourceId(vo.getId())
                    .isHalf(vo.getIsHalf())
                    .build());
        }
        if (!CollectionUtils.isEmpty(list)) {
            roleResourceMapper.insertBatch(list);
        }
    }

    /**
     * 添加角色单位关联表
     */
    private void insertRoleUnit(Role role) {
        List<CheckBoxVO> unitIds = role.getCheckedUnitIds();
        if (StringUtils.isEmpty(unitIds)) {
            return;
        }
        List<RoleUnit> list = new ArrayList<>();
        for (CheckBoxVO vo : unitIds) {
            list.add(RoleUnit.builder()
                    .roleId(role.getId())
                    .unitId(vo.getId())
                    .isHalf(vo.getIsHalf())
                    .build());
        }
        if (!CollectionUtils.isEmpty(list)) {
            roleUnitMapper.insertBatch(list);
        }
    }
}
