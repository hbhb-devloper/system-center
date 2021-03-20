package com.hbhb.cw.systemcenter.service;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.cw.systemcenter.model.Hall;
import com.hbhb.cw.systemcenter.vo.HallResVO;
import org.beetl.sql.core.page.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author xiaokang
 * @since 2021-01-05
 */
public interface HallService {

    /**
     * 分页查询营业厅列表
     */
    PageResult<HallResVO> pageHall(Integer pageNum, Integer pageSize,
                                   Integer unitId, String hallName, Boolean enable);

    /**
     * 查询营业厅列表
     */
    List<SelectVO> listHall(Integer userId,Integer unitId);

    /**
     * 查询营业厅列表
     */
    Map<String, Object> listHallNew(Integer userId,Integer unitId);

    /**
     * 新增/修改营业厅
     */
    void upsertHall(Hall hall);

    /**
     * 更新营业厅
     */
    void updateHallNew(Integer userId,Integer unitId,List<Integer> hallSelectIds);

    /**
     * 通过UnitId去获取到这个人的营业厅列表
     */
    Map<Integer, String> selectHallByUnitId(Integer unitId);


    /**
     * 通过userId去获取到这个人的营业厅列表
     */
    List<SelectVO> selectHallByUserId(Integer userId);

    /**
     * 获取单位下所有营业厅id
     *
     * @param unitId 单位id
     * @return 营业厅id
     */
    List<Integer> getSubHall(List<Integer> unitId);

    /**
     * 获取营业厅详情
     *
     * @param unitId id
     * @return 详情
     */
    Hall getHallInfo(Integer unitId);

    /**
     * 获取营业厅对应id-营业厅名称
     *
     * @return 名称
     */
    Map<Integer, String> getUnitMapById();
}
