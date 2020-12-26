package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.web.vo.HomeModuleVO;

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
}
