package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.cw.systemcenter.model.Unit;
import com.hbhb.web.beetlsql.BaseMapper;

import java.util.List;

public interface UnitMapper  extends BaseMapper<Unit> {

    List<Unit> selectByUserId(Integer userId);
}
