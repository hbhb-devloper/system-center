package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.User;
import com.hbhb.cw.systemcenter.web.vo.UserReqVO;
import com.hbhb.cw.systemcenter.web.vo.UserResVO;
import com.hbhb.web.beetlsql.BaseMapper;

import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;

/**
 * @since 2020-10-06
 */
public interface UserMapper extends BaseMapper<User> {

    PageResult<UserResVO> selectPageByCond(UserReqVO cond, PageRequest request);

}