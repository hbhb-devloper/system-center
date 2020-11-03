package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.mapper.UnitMapper;
import com.hbhb.cw.systemcenter.model.Unit;
import com.hbhb.cw.systemcenter.service.UnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UnitServiceImpl implements UnitService {
@Resource
private UnitMapper unitMapper;

    @Override
    public List<Unit> getAllUnitList() {
        return unitMapper.all();
    }
}
