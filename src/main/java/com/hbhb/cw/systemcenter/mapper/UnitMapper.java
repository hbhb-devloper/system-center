package com.hbhb.cw.systemcenter.mapper;

import com.hbhb.beetlsql.BaseMapper;
import com.hbhb.cw.systemcenter.model.Unit;

import java.util.List;

public interface UnitMapper  extends BaseMapper<Unit> {

    List<Unit> selectByUserId(Integer userId);
}
