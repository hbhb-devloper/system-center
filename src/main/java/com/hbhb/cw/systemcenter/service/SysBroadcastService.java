package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.SysBroadcast;

import org.beetl.sql.core.page.PageResult;

import java.util.List;

/**
 * @author xiaokang
 * @since 2020-09-09
 */
public interface SysBroadcastService {

    /**
     * 分页查询公告列表
     */
    PageResult<SysBroadcast> pageBroadcast(Integer pageNum, Integer pageSize,
                                           String content, Byte state);

    /**
     * 查询已发布的公告
     */
    List<String> getPublishList();

    /**
     * 新增/更新公告
     */
    void upsertBroadcast(SysBroadcast broadcast);

    /**
     * 删除公告
     */
    void deleteBroadcast(Long id);
}
