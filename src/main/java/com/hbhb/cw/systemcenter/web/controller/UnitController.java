package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.cw.systemcenter.api.UnitApi;
import com.hbhb.cw.systemcenter.model.Unit;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.service.UserService;
import com.hbhb.cw.systemcenter.vo.TreeSelectVO;
import com.hbhb.cw.systemcenter.vo.TreeSelectWrapVO;
import com.hbhb.cw.systemcenter.vo.UnitTopVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.web.annotation.UserId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "单位相关")
@RestController
@RequestMapping("/unit")
@Slf4j
public class UnitController implements UnitApi {
    @Resource
    private UnitService unitService;

    @Value("${cw.unit-id.hangzhou}")
    private Integer hangzhou;

    @Value("${cw.unit-id.benbu}")
    private Integer benbu;

    @Resource
    private UserService userService;

    @Override
    public List<Unit> getAllUnitList() {
        return unitService.getAllUnitList();
    }

    @Override
    public List<TreeSelectVO> getUnitList(String s) {
        return null;
    }

    @Override
    public List<Integer> getRoleResourceTreeSelect(Integer integer) {
        return null;
    }


    @GetMapping("/tree-select")
    public TreeSelectWrapVO getUnitTreeSelect(@UserId Integer userId) {
        // 每次需要从数据库拿最新的 defaultUnitId
        UserInfo user = userService.getUserById(userId);
        // 用户的单位范围列表
        List<TreeSelectVO> list = unitService.getUnitTreeSelectByUser(user.getId());
        return TreeSelectWrapVO.builder()
                .checked(Collections.singletonList(user.getUnitId()))
                .list(list).build();
    }

    @Override
    public Unit getUnitInfo(Integer integer) {
        return null;
    }

    @Override
    public void addUnit(Unit unit) {

    }

    @Override
    public void updateUnit(Unit unit) {

    }

    @Override
    public void deleteUnit(Integer integer) {
    }

    @Override
    public UnitTopVO getTopUnit() {
        return UnitTopVO.builder()
                .hangzhou(hangzhou)
                .benbu(benbu)
                .build();
    }
}
