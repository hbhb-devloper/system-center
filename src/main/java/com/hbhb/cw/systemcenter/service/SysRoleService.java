package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.SysRole;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;
import com.hbhb.cw.systemcenter.web.vo.RoleVO;

import org.beetl.sql.core.page.PageResult;

import java.util.List;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface SysRoleService {

    /**
     * 按条件获取角色列表（分页）
     */
    PageResult<RoleVO> getRolePageByCond(Integer pageNum, Integer pageSize, RoleVO vo);

    /**
     * 按类型获取所有的角色列表
     */
    List<TreeSelectVO> getRoleListByType(String roleType);

    /**
     * 获取用户所对应的角色id
     */
    List<Integer> getCheckedRoleByUser(Integer userId, String roleType);

    /**
     * 获取角色详情
     */
    SysRole getRoleInfo(Integer roleId);

    /**
     * 新增角色
     */
    void addRole(SysRole role);

    /**
     * 更新角色
     */
    void updateRole(SysRole role);

    /**
     * 转换角色状态
     */
    void changeState(Integer roleId, Byte state);

    /**
     * 获取用户的所有角色id
     */
    List<Integer> getRolesByUserId(Integer userId);

    /**
     * 判断某用户是否为管理员角色
     */
    boolean isAdminRole(Integer userId);
}
