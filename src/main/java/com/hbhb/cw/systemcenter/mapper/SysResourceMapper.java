package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.SysResource;
import com.hbhb.cw.systemcenter.vo.SysResourceVO;
import com.hbhb.web.beetlsql.BaseMapper;

import java.util.List;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<SysResourceVO> selectAll();

    List<String> selectPermsByUserId(Integer userId);
}
