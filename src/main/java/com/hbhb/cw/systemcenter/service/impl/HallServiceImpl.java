package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.beetlsql.core.QueryExt;
import com.hbhb.cw.systemcenter.mapper.HallMapper;
import com.hbhb.cw.systemcenter.model.Hall;
import com.hbhb.cw.systemcenter.service.HallService;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.vo.HallResVO;

import org.beetl.sql.core.page.PageResult;
import org.beetl.sql.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2021-01-05
 */
@Service
public class HallServiceImpl implements HallService {

    @Resource
    private HallMapper hallMapper;
    @Resource
    private UnitService unitService;

    @Override
    public PageResult<HallResVO> pageHall(Integer pageNum, Integer pageSize,
                                          Integer unitId, String hallName, Boolean enable) {
        PageResult<HallResVO> page = hallMapper.createLambdaQuery()
                .andEq(Hall::getUnitId, Query.filterNull(unitId))
                .andLike(Hall::getHallName, QueryExt.filterLikeEmpty(hallName))
                .andEq(Hall::getEnable, Query.filterNull(enable))
                .page(pageNum, pageSize, HallResVO.class);
        page.getList().forEach(vo -> vo.setUnitName(unitService.getUnitName(vo.getUnitId())));
        return page;
    }

    @Override
    public List<SelectVO> listHall(Integer unitId) {
        List<Hall> list = hallMapper.createLambdaQuery()
                .andEq(Hall::getUnitId, unitId)
                .andEq(Hall::getEnable, true)
                .select();
        return Optional.ofNullable(list)
                .orElse(new ArrayList<>())
                .stream()
                .map(hall -> SelectVO.builder()
                        .id(Long.valueOf(hall.getId()))
                        .label(hall.getHallName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void upsertHall(Hall hall) {
        hallMapper.upsertByTemplate(hall);
    }
}
