package com.hbhb.cw.systemcenter.config;

import com.hbhb.cw.systemcenter.service.SysResourceService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 容器启动后加载资源权限数据到缓存
 *
 * @author dxk
 */
@Component
@Slf4j
public class InitPermsCacheRunner implements CommandLineRunner {

    @Resource
    private SysResourceService sysResourceService;

    @Override
    public void run(String... args) {
        log.info("InitResourceRolesCacheRunner run...");
        sysResourceService.cachePerms();
    }
}
