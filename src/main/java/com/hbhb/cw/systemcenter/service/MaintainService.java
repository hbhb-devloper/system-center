package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.web.vo.MaintainVO;

public interface MaintainService {

    /**
     * 查询系统维护信息详情
     */
    MaintainVO getMaintainInfo();

    /**
     * 修改系统维护信息
     */
    void updateMaintain(MaintainVO vo, Integer userId);
}
