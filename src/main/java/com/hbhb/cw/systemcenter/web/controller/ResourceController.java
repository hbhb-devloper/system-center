package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.cw.systemcenter.service.ResourceService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaokang
 * @since 2020-10-14
 */
@Tag(name = "资源相关")
@RestController
@RequestMapping("/resource")
@Slf4j
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @Operation(summary = "同步资源权限到redis")
    @GetMapping("/cache")
    public void cachePerms() {
        resourceService.cachePerms();
    }
}
