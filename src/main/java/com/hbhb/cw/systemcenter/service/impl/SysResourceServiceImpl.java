package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.constants.AuthConstant;
import com.hbhb.cw.systemcenter.mapper.SysResourceMapper;
import com.hbhb.cw.systemcenter.service.SysResourceService;
import com.hbhb.cw.systemcenter.vo.SysResourceVO;
import com.hbhb.redis.component.RedisHelper;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2020-10-14
 */
@Service
public class SysResourceServiceImpl implements SysResourceService {

    @Resource
    private RedisHelper redisHelper;
    @Resource
    private SysResourceMapper sysResourceMapper;

    @Override
    public void cachePerms() {
        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
        // 先删除旧的map
        redisHelper.delete(AuthConstant.RESOURCE_ROLES_KEY.value());

        List<SysResourceVO> resources = sysResourceMapper.selectAll();
        Optional.ofNullable(resources).orElse(new ArrayList<>()).forEach(resource -> {
            // 转换 roleId -> ROLE_{roleId}
            List<String> roles = Optional.ofNullable(resource.getSrr()).orElse(new ArrayList<>())
                    .stream().map(srr -> AuthConstant.AUTHORITY_PREFIX.value() + srr.getRoleId())
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(roles)) {
                resourceRolesMap.put(resource.getApiPath(), roles);
            }
            redisHelper.setMap(AuthConstant.RESOURCE_ROLES_KEY.value(), resourceRolesMap);
        });
    }

    @Override
    public List<String> getPermsByUserId(Integer userId) {
        return sysResourceMapper.selectPermsByUserId(userId);
    }
}
