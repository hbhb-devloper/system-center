package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.cw.systemcenter.model.SysResource;
import com.hbhb.cw.systemcenter.service.SysResourceService;
import com.hbhb.cw.systemcenter.vo.ResourceResVO;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaokang
 * @since 2020-10-14
 */
@Tag(name = "资源")
@RestController
@RequestMapping("/resource")
@Slf4j
public class SysResourceController {

    @Resource
    private SysResourceService sysResourceService;

    @Operation(summary = "获取资源列表",
            description = "资源列表以树形结构展示。此页面通常只会对内部管理员开放，所以通常不需要业务查询功能")
    @GetMapping("/list")
    public List<ResourceResVO> listResourceByCond() {
        return sysResourceService.getAllResourceList();
    }

    @Operation(summary = "获取所有的资源列表", description = "下拉框选择时用，树形结构")
    @GetMapping("/tree-select")
    public List<TreeSelectVO> getResourceTreeSelect() {
        return sysResourceService.getResourceTreeSelect();
    }

    @Operation(summary = "获取角色的资源列表树", description = "按角色id查询对应的资源列表，树形结构")
    @GetMapping("/role/{roleId}")
    public List<Integer> getRoleResourceList(
            @Parameter(description = "角色id", required = true) @PathVariable Integer roleId) {
        return sysResourceService.getCheckedResourceByRole(roleId);
    }

    @Operation(summary = "获取资源详情")
    @GetMapping("/{resourceId}")
    public SysResource getResourceInfo(
            @Parameter(description = "资源id", required = true) @PathVariable Integer resourceId) {
        return sysResourceService.getResourceInfo(resourceId);
    }

    @Operation(summary = "添加资源")
    @PostMapping("")
    public void addResource(
            @Parameter(description = "资源信息", required = true) @RequestBody SysResource sysResource) {
        sysResourceService.upsertResource(sysResource);
    }

    @Operation(summary = "更新资源")
    @PutMapping("")
    public void updateResource(
            @Parameter(description = "资源信息", required = true) @RequestBody SysResource sysResource) {
        sysResource.setUpdateTime(new Date());
        sysResourceService.upsertResource(sysResource);
    }

    @Operation(summary = "删除资源")
    @DeleteMapping("/{resourceId}")
    public void deleteResource(
            @Parameter(description = "资源id", required = true) @PathVariable Integer resourceId) {
        sysResourceService.deleteResource(resourceId);
    }

//    @Operation(summary = "同步资源权限到redis")
//    @GetMapping("/cache")
//    public void cachePerms() {
//        resourceService.cachePerms();
//    }
}
