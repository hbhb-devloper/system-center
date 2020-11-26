package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.Role;
import com.hbhb.web.beetlsql.BaseMapper;

import java.util.List;

/**
 * @author dxk
 * @since 2020-10-06
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectUserRolesByType(Integer userId, String roleType);
}