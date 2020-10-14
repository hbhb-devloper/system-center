package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.SysRole;

import org.beetl.sql.mapper.BaseMapper;

import java.util.List;

/**
 * @author dxk
 * @since 2020-10-06
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectUserRolesByType(Integer userId, String roleType);
}