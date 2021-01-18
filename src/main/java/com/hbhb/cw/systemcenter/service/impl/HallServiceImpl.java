package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.beetlsql.core.QueryExt;
import com.hbhb.cw.systemcenter.mapper.HallMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserUintHallMapper;
import com.hbhb.cw.systemcenter.model.Hall;
import com.hbhb.cw.systemcenter.model.SysUserUintHall;
import com.hbhb.cw.systemcenter.service.HallService;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.vo.HallResVO;

import org.beetl.sql.core.page.PageResult;
import org.beetl.sql.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2021-01-05
 *
 */
@Service
public class HallServiceImpl implements HallService {

    @Resource
    private HallMapper hallMapper;
    @Resource
    private UnitService unitService;
    @Resource
    private SysUserUintHallMapper sysUserUintHallMapper;


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
    public List<SelectVO> listHall(Integer userId,Integer unitId) {

        List<Integer> integers = unitService.getSubUnit(unitId);
        return  hallMapper.createLambdaQuery()
                .andIn(Hall::getUnitId,integers)
                .andEq(Hall::getEnable, true)
                .select()
                .stream()
                .map(hall -> SelectVO.builder()
                        .id(Long.valueOf(hall.getId()))
                        .label(hall.getHallName())
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    public Map<String, Object> listHallNew(Integer userId,Integer unitId) {
        //查询当前的单位下面有没有子集
        List<Integer> integers = unitService.getSubUnit(unitId);

        //拿到单位下面的营业厅
        List<SelectVO> selectVOS =  hallMapper.createLambdaQuery()
                .andIn(Hall::getUnitId,integers)
                .andEq(Hall::getEnable, true)
                .select()
                .stream().map(hall -> SelectVO.builder()
                        .id(Long.valueOf(hall.getId()))
                        .label(hall.getHallName())
                        .build())
                .collect(Collectors.toList());

        //查询当前用户勾选营业厅
        List<Integer> sysUserUintHalls =  sysUserUintHallMapper
                .createLambdaQuery()
                .andEq(SysUserUintHall::getUserId,userId)
                .andEq(SysUserUintHall::getUintId,unitId)
                .select()
                .stream()
                .map(SysUserUintHall::getHallId)
                .collect(Collectors.toList());

        Map<String,Object> map = new HashMap<>();
        map.put("halls", selectVOS);
        map.put("hallSelect",sysUserUintHalls);
        return map;
    }

    @Override
    public void upsertHall(Hall hall) {
        hallMapper.upsertByTemplate(hall);
    }

    @Override
    public void updateHallNew(Integer userId,Integer unitId, List<Integer> hallSelectIds) {
        //通过userId,unitId,hallSelectIds关联被选择的部分
        sysUserUintHallMapper.insertBatch(
                Optional.of(hallSelectIds)
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(id -> SysUserUintHall
                                .builder()
                                .userId(userId)
                                .uintId(unitId)
                                .hallId(id)
                                .build())
                        .collect(Collectors.toList()));
    }

    /**
     * 通过当前这个人的userId，查询到当前这个人的单位
     * @param unitId 菜单id
     * @return 营业厅列表
     */
    @Override
    public Map<Integer,String> selectHallByUnitId(Integer unitId) {
        List<Integer> integers = unitService.getSubUnit(unitId);

        //拿到单位下面的营业厅
        List<Integer> unitIds =  hallMapper.createLambdaQuery()
                .andIn(Hall::getUnitId,integers)
                .andEq(Hall::getEnable, true)
                .select()
                .stream().map(Hall::getUnitId)
                .collect(Collectors.toList());

        if (unitIds.isEmpty()) return new HashMap<>();
        return hallMapper.createLambdaQuery().andIn(Hall::getUnitId,unitIds)
                .select()
                .stream()
                .collect(Collectors.toMap(Hall::getId,Hall::getHallName));
    }


}
