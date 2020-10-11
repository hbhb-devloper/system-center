package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.SysResource;

import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<String> selectUserPermsByType(Integer userId, List<String> rsTypes);
}
