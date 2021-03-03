package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.beetlsql.core.QueryExt;
import com.hbhb.cw.systemcenter.enums.UnitEnum;
import com.hbhb.cw.systemcenter.mapper.HallMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserUnitHallMapper;
import com.hbhb.cw.systemcenter.model.Hall;
import com.hbhb.cw.systemcenter.model.SysUserUnitHall;
import com.hbhb.cw.systemcenter.service.HallService;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.vo.HallResVO;
import org.beetl.sql.core.page.PageResult;
import org.beetl.sql.core.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xiaokang
 * @since 2021-01-05
 *
 */
@Service
public class HallServiceImpl implements HallService {

    private final  static Logger logger = LoggerFactory.getLogger(HallServiceImpl.class);

    @Resource
    private HallMapper hallMapper;
    @Resource
    private UnitService unitService;
    @Resource
    private SysUserUnitHallMapper sysUserUnitHallMapper;


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
        //如果是本部，就需要通过unitUId查询
        List<Integer> sysUserUintHalls ;
        if (unitId.equals(UnitEnum.HANGZHOU.value()) || unitId.equals(UnitEnum.BENBU.value())){
            sysUserUintHalls = sysUserUnitHallMapper
                    .createLambdaQuery()
                    .andEq(SysUserUnitHall::getUserId, userId)
//                    .andEq(SysUserUnitHall::getUintId,unitId)
                    .select()
                    .stream()
                    .filter(sysUserUintHall -> sysUserUintHall.getHallId() != null)
                    .map(SysUserUnitHall::getHallId)
                    .collect(Collectors.toList())
                    .stream().distinct()
                    .collect(Collectors.toList());
        }else {
            sysUserUintHalls = sysUserUnitHallMapper
                    .createLambdaQuery()
                    .andEq(SysUserUnitHall::getUserId, userId)
                    .andEq(SysUserUnitHall::getUnitId, unitId)
                    .select()
                    .stream()
                    .filter(sysUserUintHall -> sysUserUintHall.getHallId() != null)
                    .map(SysUserUnitHall::getHallId)
                    .collect(Collectors.toList())
                    .stream().distinct()
                    .collect(Collectors.toList());
        }

        logger.info("sysUserUintHalls===============>{}",sysUserUintHalls);

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
        sysUserUnitHallMapper.createLambdaQuery()
                .andEq(SysUserUnitHall::getUserId, userId)
                .andEq(SysUserUnitHall::getUnitId, unitId)
                .delete();
        //通过userId,unitId,hallSelectIds关联被选择的部分
        sysUserUnitHallMapper.insertBatch(
                Optional.of(hallSelectIds)
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(id -> SysUserUnitHall
                                .builder()
                                .userId(userId)
                                .unitId(unitId)
                                .hallId(id)
                                .build())
                        .collect(Collectors.toList()));
    }

    /**
     * 通过当前这个人的unitId，查询到当前这个人的单位
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

        if (unitIds.isEmpty()) {
            return new HashMap<>();
        }
        return hallMapper.createLambdaQuery().andIn(Hall::getUnitId,unitIds)
                .select()
                .stream()
                .collect(Collectors.toMap(Hall::getId,Hall::getHallName));
    }

    @Override
    public List<SelectVO> selectHallByUserId(Integer userId) {

        //拿到用户下的单位
        List<Integer> integers = sysUserUnitHallMapper.createLambdaQuery()
                .andEq(SysUserUnitHall::getUserId, userId)
                .select()
                .stream()
                .map(SysUserUnitHall::getHallId)
                .distinct()
                .collect(Collectors.toList());

        if (integers.isEmpty()) {
            return new ArrayList<>();
        }
        return hallMapper.createLambdaQuery().andIn(Hall::getId, integers)
                .select()
                .stream()
                .map(hall -> SelectVO.builder().id(Long.valueOf(hall.getId())).label(hall.getHallName()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getSubHall(List<Integer> unitId) {
        List<Integer> hallIds = new ArrayList<>();
        List<Hall> select = hallMapper.createLambdaQuery().andIn(Hall::getUnitId, unitId).select();
        select.forEach(item -> hallIds.add(item.getId()));
        return hallIds;
    }

    @Override
    public Hall getHallInfo(Integer unitId) {
        return hallMapper.single(unitId);
    }


}
