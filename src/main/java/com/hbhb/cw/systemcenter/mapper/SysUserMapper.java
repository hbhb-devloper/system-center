package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.beetlsql.BaseMapper;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.vo.UserReqVO;
import com.hbhb.cw.systemcenter.vo.UserResVO;

import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.beetl.sql.mapper.annotation.Param;

/**
 * @since 2020-10-06
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    PageResult<UserResVO> selectPageByCond(@Param("cond") UserReqVO cond, PageRequest request);

}