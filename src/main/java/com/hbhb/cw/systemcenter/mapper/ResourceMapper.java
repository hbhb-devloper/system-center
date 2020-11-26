package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.Resource;
import com.hbhb.cw.systemcenter.web.vo.ResourceVO;
import com.hbhb.web.beetlsql.BaseMapper;

import java.util.List;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    List<ResourceVO> selectAll();

    List<String> selectPermsByUserId(Integer userId);
}
