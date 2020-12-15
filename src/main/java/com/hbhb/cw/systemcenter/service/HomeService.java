package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.web.vo.HomeModuleVO;
import com.hbhb.cw.systemcenter.web.vo.HomeTodoVO;

import java.util.List;

/**
 * @author wxg
 * @since 2020-09-30
 */
public interface HomeService {

    /**
     * 获取首页统计列表
     */
    List<HomeModuleVO> getModuleList(Integer userId);

    /**
     * 获取各模块的待办列表
     */
    List<HomeTodoVO> getTodoList(Integer module, Integer userId);
}
