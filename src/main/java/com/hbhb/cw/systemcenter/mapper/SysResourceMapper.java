package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.beetlsql.BaseMapper;
import com.hbhb.cw.systemcenter.model.SysResource;

import org.beetl.sql.mapper.annotation.Param;

import java.util.List;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface SysResourceMapper extends BaseMapper<SysResource> {

    List<String> selectPermsByUserId(Integer userId, @Param("list") List<String> types);

    List<SysResource> selectMenuTreeAll(@Param("list") List<String> types);

    List<SysResource> selectMenuTreeByUserId(Integer userId, @Param("list") List<String> types);
}
