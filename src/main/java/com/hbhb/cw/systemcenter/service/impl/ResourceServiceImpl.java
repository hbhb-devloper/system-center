package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.bean.BeanConverter;
import com.hbhb.core.constants.AuthConstant;
import com.hbhb.core.utils.TreeUtil;
import com.hbhb.cw.systemcenter.enums.ResourceType;
import com.hbhb.cw.systemcenter.mapper.ResourceMapper;
import com.hbhb.cw.systemcenter.mapper.RoleResourceMapper;
import com.hbhb.cw.systemcenter.model.RoleResource;
import com.hbhb.cw.systemcenter.model.SysResource;
import com.hbhb.cw.systemcenter.service.ResourceService;
import com.hbhb.cw.systemcenter.service.RoleService;
import com.hbhb.cw.systemcenter.vo.MetaVO;
import com.hbhb.cw.systemcenter.vo.ResourceResVO;
import com.hbhb.cw.systemcenter.vo.RouterVO;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;
import com.hbhb.redis.component.RedisHelper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2020-10-14
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Resource
    private RedisHelper redisHelper;
    @Resource
    private ResourceMapper resourceMapper;
    @Resource
    private RoleResourceMapper roleResourceMapper;
    @Resource
    private RoleService roleService;

    @Override
    public List<ResourceResVO> getAllResourceList() {
        List<SysResource> list = resourceMapper.createLambdaQuery()
                // todo 测试
                .asc(SysResource::getParentId)
                .asc(SysResource::getOrderNum)
                .select();
        List<ResourceResVO> result = BeanConverter.copyBeanList(list, ResourceResVO.class);
        return TreeUtil.build(result);
    }

    @Override
    public List<TreeSelectVO> getResourceTreeSelect() {
        // 查询所有的资源列表
        List<SysResource> list = resourceMapper.createLambdaQuery()
                .asc(SysResource::getParentId)
                .asc(SysResource::getOrderNum)
                .select();
        // 转换成树形结构
        List<SysResource> treeList = TreeUtil.build(list);
        if (CollectionUtils.isEmpty(treeList)) {
            return new ArrayList<>();
        }
        // 转KV
        return treeList.stream().map(TreeSelectVO::new).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getCheckedResourceByRole(Integer roleId) {
        List<RoleResource> list = roleResourceMapper.createLambdaQuery()
                .andEq(RoleResource::getRoleId, roleId)
                .select();
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(RoleResource::getResourceId).collect(Collectors.toList());
    }

    @Override
    public SysResource getResourceInfo(Integer resourceId) {
        return resourceMapper.single(resourceId);
    }

    @Override
    public Set<String> getUserPermission(Integer userId, List<String> types) {
        Set<String> perms = new HashSet<>();
        // 判断用户角色是否为管理员
        boolean isAdmin = roleService.isAdminRole(userId);
        if (isAdmin) {
            // 管理员拥有所有权限
            perms.add(AuthConstant.ALL_PERMISSION.value());
        } else {
            perms.addAll(resourceMapper.selectPermsByUserId(userId, types));
        }
        return perms;
    }

    @Override
    public List<RouterVO> getNavMenuTreeByUserId(Integer userId) {
        List<SysResource> list;
        List<String> rsTypes = Arrays.asList(ResourceType.NAVIGATION_CONTENT.value(),
                ResourceType.NAVIGATION_MENU.value());
        // 判断用户角色是否为管理员
        boolean isAdmin = roleService.isAdminRole(userId);
        if (isAdmin) {
            // 管理员拥有所有权限
            list = resourceMapper.selectMenuTreeAll(rsTypes);
        } else {
            list = resourceMapper.selectMenuTreeByUserId(userId, rsTypes);
        }
        return buildRouters(TreeUtil.build(list));
    }

    @Override
    public List<RouterVO> getSideMenuTreeByUserId(Integer userId) {
        List<SysResource> list;
        List<String> rsTypes = Arrays.asList(ResourceType.SIDE_CONTENT.value(),
                ResourceType.SIDE_MENU.value());
        // 判断用户角色是否为管理员
        boolean isAdmin = roleService.isAdminRole(userId);
        if (isAdmin) {
            // 管理员拥有所有权限
            list = resourceMapper.selectMenuTreeAll(rsTypes);
        } else {
            list = resourceMapper.selectMenuTreeByUserId(userId, rsTypes);
        }
        return buildRouters(TreeUtil.build(list));
    }

    @Override
    public void upsertResource(SysResource sysResource) {
        resourceMapper.upsertByTemplate(sysResource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteResource(Integer resourceId) {
        // 先删除关联表数据
        roleResourceMapper.createLambdaQuery()
                .andEq(RoleResource::getResourceId, resourceId)
                .delete();
        // 再删除主表数据
        resourceMapper.deleteById(resourceId);
    }

//    @Override
//    public void cachePerms() {
//        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
//        // 先删除旧的map
//        redisHelper.delete(AuthConstant.RESOURCE_ROLES_KEY.value());
//
//        List<ResourceVO> resources = resourceMapper.selectAll();
//        Optional.ofNullable(resources).orElse(new ArrayList<>()).forEach(resource -> {
//            // 转换 roleId -> ROLE_{roleId}
//            List<String> roles = Optional.ofNullable(resource.getSrr()).orElse(new ArrayList<>())
//                    .stream().map(srr -> AuthConstant.AUTHORITY_PREFIX.value() + srr.getRoleId())
//                    .collect(Collectors.toList());
//            if (!CollectionUtils.isEmpty(roles)) {
//                resourceRolesMap.put(resource.getApiPath(), roles);
//            }
//            redisHelper.setMap(AuthConstant.RESOURCE_ROLES_KEY.value(), resourceRolesMap);
//        });
//    }

    /**
     * 资源转为router
     */
    private List<RouterVO> buildRouters(List<SysResource> list) {
        List<RouterVO> routers = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (SysResource resource : list) {
                RouterVO router = new RouterVO();
                router.setName(getRouteName(resource));
                router.setPath(getRouterPath(resource));
                router.setComponent(getComponent(resource));
                router.setHidden(resource.getVisible() == 0);
                router.setMeta(new MetaVO(resource.getRsName(), resource.getIcon()));
                List<SysResource> children = resource.getChildren();
                // 判断是否为目录
                if (!CollectionUtils.isEmpty(children) && ResourceType.isContent(resource.getRsType())) {
                    // 父菜单需要的参数
                    router.setAlwaysShow(true);
                    router.setRedirect("noRedirect");
                }
                router.setChildren(buildRouters(children));
                routers.add(router);
            }
        }
        return routers;
    }

    /**
     * 获取路由名称
     */
    private String getRouteName(SysResource resource) {
        String routerName = StringUtils.capitalize(resource.getPath());
        // 非外链，且是一级目录（类型为目录）
        if (isMenuFrame(resource)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     */
    private String getRouterPath(SysResource resource) {
        String routerPath = resource.getPath();
        // 非外链，且是一级目录（类型为目录）
        if (resource.getParentId() == 0
                && ResourceType.isContent(resource.getRsType())
                && resource.getIsFrame().equals(new Byte(AuthConstant.NO_FRAME.value()))) {
            routerPath = "/" + resource.getPath();
        }
        // 非外链，且是一级目录（类型为菜单）
        else if (isMenuFrame(resource)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     */
    private String getComponent(SysResource resource) {
        String component = AuthConstant.LAYOUT.value();
        if (StringUtils.isNotEmpty(resource.getComponent()) && !isMenuFrame(resource)) {
            component = resource.getComponent();
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     */
    private boolean isMenuFrame(SysResource resource) {
        return resource.getParentId() == 0
                && ResourceType.isMenu(resource.getRsType())
                && resource.getIsFrame().equals(new Byte(AuthConstant.NO_FRAME.value()));
    }
}
