package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.Role;
import com.hbhb.cw.systemcenter.web.vo.RoleVO;
import com.hbhb.web.beetlsql.BaseMapper;

import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;

import java.util.List;

/**
 * @author dxk
 * @since 2020-10-06
 */
public interface RoleMapper extends BaseMapper<Role> {

    PageResult<RoleVO> selectPageByCond(RoleVO vo, PageRequest request);

    List<Role> selectUserRolesByType(Integer userId, String roleType);

    List<Integer> selectUnitRoleByUserId(Integer userId);
}