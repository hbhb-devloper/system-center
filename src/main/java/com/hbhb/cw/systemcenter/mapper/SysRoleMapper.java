package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.SysRole;

import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author dxk
 * @since 2020-10-06
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectUserRolesByType(Integer userId, String roleType);
}