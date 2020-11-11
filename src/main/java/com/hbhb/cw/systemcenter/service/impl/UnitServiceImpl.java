package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.utils.TreeUtil;
import com.hbhb.cw.systemcenter.mapper.UnitMapper;
import com.hbhb.cw.systemcenter.model.Unit;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UnitServiceImpl implements UnitService {
@Resource
private UnitMapper unitMapper;

    @Override
    public List<Unit> getAllUnitList() {
        return unitMapper.all();
    }

    @Override
    public List<TreeSelectVO> getUnitTreeSelectByUser(Integer userId) {
        // 查询用户的单位列表（按parentId升序）
        List<Unit> units = unitMapper.selectByUserId(userId);
        if (CollectionUtils.isEmpty(units)) {
            return new ArrayList<>();
        }
        // 转换成树形结构
        // 以第一条数据的 parentId 作为 rootId 进行树形构建
        Integer rootId = units.get(0).getParentId();
        List<Unit> treeList = TreeUtil.build(units, rootId.toString());
        if (CollectionUtils.isEmpty(treeList)) {
            return new ArrayList<>();
        }
        // 转KV
        return treeList.stream().map(TreeSelectVO::new).collect(Collectors.toList());
    }
}
