package com.hbhb.cw.systemcenter.service;

import java.util.List;

/**
 * @author xiaokang
 * @since 2020-10-14
 */
public interface ResourceService {

    /**
     * 保存资源角色对应关系到redis
     */
    void cachePerms();

    /**
     * 获取某用户的所有权限
     */
    List<String> getPermsByUserId(Integer userId);
}
