package com.hbhb.cw.systemcenter.service;

import com.hbhb.cw.systemcenter.model.SysResource;
import com.hbhb.cw.systemcenter.vo.ResourceResVO;
import com.hbhb.cw.systemcenter.vo.RouterVO;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;

import java.util.List;
import java.util.Set;

/**
 * @author xiaokang
 * @since 2020-10-14
 */
public interface ResourceService {

    /**
     * 获取全部资源列表（树形结构、全字段）
     */
    List<ResourceResVO> getAllResourceList();

    /**
     * 获取所有的资源列表（树形结构、下拉框用、KV）
     */
    List<TreeSelectVO> getResourceTreeSelect();

    /**
     * 获取角色所对应的资源id
     */
    List<Integer> getCheckedResourceByRole(Integer roleId);

    /**
     * 获取资源详情
     */
    SysResource getResourceInfo(Integer resourceId);

    /**
     * 获取登录用户的权限（按类型）
     */
    Set<String> getUserPermission(Integer userId, List<String> types);

    /**
     * 获取登录用户的导航菜单（树形结构）
     */
    List<RouterVO> getNavMenuTreeByUserId(Integer userId);

    /**
     * 获取登录用户的侧边菜单（树形结构）
     */
    List<RouterVO> getSideMenuTreeByUserId(Integer userId);

    /**
     * 新增/更新资源
     */
    void upsertResource(SysResource sysResource);

    /**
     * 删除资源
     */
    void deleteResource(Integer resourceId);

//    /**
//     * 保存资源角色对应关系到redis
//     */
//    void cachePerms();
}
