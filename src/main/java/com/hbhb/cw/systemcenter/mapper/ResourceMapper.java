package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.SysResource;
import com.hbhb.web.beetlsql.BaseMapper;

import org.beetl.sql.mapper.annotation.Param;

import java.util.List;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface ResourceMapper extends BaseMapper<SysResource> {

    List<SysResource> selectAll();

    List<String> selectPermsByUserId(Integer userId, @Param("list") List<String> types);

    List<SysResource> selectMenuTreeAll(@Param("list") List<String> types);

    List<SysResource> selectMenuTreeByUserId(Integer userId, @Param("list") List<String> types);
}
