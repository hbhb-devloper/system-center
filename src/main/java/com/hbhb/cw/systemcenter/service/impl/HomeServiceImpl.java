package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.enums.DictCode;
import com.hbhb.cw.systemcenter.enums.Module;
import com.hbhb.cw.systemcenter.enums.TypeCode;
import com.hbhb.cw.systemcenter.rpc.BudgetApiExp;
import com.hbhb.cw.systemcenter.rpc.FlowApiExp;
import com.hbhb.cw.systemcenter.rpc.FundApiExp;
import com.hbhb.cw.systemcenter.rpc.WarnApiExp;
import com.hbhb.cw.systemcenter.service.HomeService;
import com.hbhb.cw.systemcenter.service.SysDictService;
import com.hbhb.cw.systemcenter.service.SysUserService;
import com.hbhb.cw.systemcenter.vo.DictVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.web.vo.HomeModuleVO;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wxg
 * @since 2020-09-30
 */
@Service
@Slf4j
public class HomeServiceImpl implements HomeService {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private FundApiExp fundApi;
    @Resource
    private BudgetApiExp budgetApi;
    @Resource
    private WarnApiExp warnApiExp;
    @Resource
    private FlowApiExp flowApi;

    @Override
    public List<HomeModuleVO> getModuleList(Integer userId) {
        UserInfo userInfo = sysUserService.getUserInfoById(userId);
        List<HomeModuleVO> workList = new ArrayList<>();
        List<DictVO> moduleList = sysDictService.listDictByCond(
                TypeCode.MODULE.value(), DictCode.MODULE.value());
        Map<String, String> moduleMap = moduleList.stream().collect(
                Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        // 预算提醒统计
        HomeModuleVO module1 = new HomeModuleVO();
        module1.setModule(Module.MODULE_BUDGET.getValue());
        module1.setModuleName(moduleMap.get(Module.MODULE_BUDGET.getValue().toString()));
        module1.setCount(budgetApi.countNotice(userId));
        workList.add(module1);

        // 客户资金提醒统计
        HomeModuleVO module2 = new HomeModuleVO();
        module2.setModule(Module.MODULE_INVOICE.getValue());
        module2.setModuleName(moduleMap.get(Module.MODULE_INVOICE.getValue().toString()));
        module2.setCount(fundApi.countNotice(userId));
        workList.add(module2);

        // 迁改预警提醒统计
        List<String> roleName = flowApi.getRoleNameByUserId(userId);
        if (roleName.contains("迁改预警负责人")) {
            HomeModuleVO module3 = new HomeModuleVO();
            module3.setModule(Module.MODULE_RELOCATION.getValue());
            module3.setModuleName(moduleMap.get(Module.MODULE_RELOCATION.getValue().toString()));
            module3.setCount(warnApiExp.countWarn(userInfo.getUnitId()));
            workList.add(module3);
        }
        return workList;
    }
}
