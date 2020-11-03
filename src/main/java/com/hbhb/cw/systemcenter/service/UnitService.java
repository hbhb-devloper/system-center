package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.Unit;

import java.util.List;

public interface UnitService {
    /**
     * 查询所有单位列表（KV）
     */
    List<Unit> getAllUnitList();
}
