package com.hbhb.cw.systemcenter.service;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.cw.systemcenter.model.Hall;
import com.hbhb.cw.systemcenter.vo.HallResVO;

import org.beetl.sql.core.page.PageResult;

import java.util.List;

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
    List<SelectVO> listHall(Integer unitId);

    /**
     * 新增/修改营业厅
     */
    void upsertHall(Hall hall);
}
