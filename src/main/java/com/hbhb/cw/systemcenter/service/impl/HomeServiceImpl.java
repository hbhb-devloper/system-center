package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.enums.DictCode;
import com.hbhb.cw.systemcenter.enums.Module;
import com.hbhb.cw.systemcenter.enums.TypeCode;
import com.hbhb.cw.systemcenter.rpc.*;
import com.hbhb.cw.systemcenter.service.HomeService;
import com.hbhb.cw.systemcenter.service.SysDictService;
import com.hbhb.cw.systemcenter.service.SysUserService;
import com.hbhb.cw.systemcenter.vo.DictVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.web.vo.HomeModuleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Resource
    private PublicityMaterialsApiExp materialsApi;
    @Resource
    private PublicityPictureApiExp pictureApi;
    @Resource
    private PublicityPrintApiExp printApi;
    @Resource
    private PublicityApplicationApiExp applicationApi;
    @Resource
    private PublicityVerifyApiExp verifyApi;
    @Resource
    private ReportApiExp reportApiExp;


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
        Long aLong = budgetApi.countNotice(userId);
        if (aLong != 0) {
            module1.setCount(aLong);
            workList.add(module1);
        }

        // 客户资金提醒统计
        HomeModuleVO module2 = new HomeModuleVO();
        module2.setModule(Module.MODULE_INVOICE.getValue());
        module2.setModuleName(moduleMap.get(Module.MODULE_INVOICE.getValue().toString()));
        Long aLong1 = fundApi.countNotice(userId);
        if (aLong1 != 0) {
            module2.setCount(aLong1);
            workList.add(module2);
        }
        // 迁改预警提醒统计
        List<String> roleName = flowApi.getRoleNameByUserId(userId);
        if (roleName.contains("迁改预警负责人")) {
            HomeModuleVO module3 = new HomeModuleVO();
            module3.setModule(Module.MODULE_RELOCATION.getValue());
            module3.setModuleName(moduleMap.get(Module.MODULE_RELOCATION.getValue().toString()));
            module3.setCount(warnApiExp.countWarn(userInfo.getUnitId()));
            workList.add(module3);
        }

        // 宣传用品提醒统计
        HomeModuleVO module4 = new HomeModuleVO();
        module4.setModule(Module.MODULE_PUBLICITY.getValue());
        module4.setModuleName(moduleMap.get(Module.MODULE_PUBLICITY.getValue().toString()));
        Long count = pictureApi.countNotice(userId);
        Long count1 = printApi.countNotice(userId);
        Long count2 = materialsApi.countNotice(userId);
        Long count3 = applicationApi.countNotice(userId);
        Long count4 = verifyApi.countNotice(userId);
        Long total = count + count1 + count2 + count3 + count4;
        if (total != 0) {
            module4.setCount(total);
            workList.add(module4);
        }
        // 报表管理提醒统计
        HomeModuleVO module5 = new HomeModuleVO();
        module5.setModule(Module.MODULE_REPORT.getValue());
        module5.setModuleName(moduleMap.get(Module.MODULE_REPORT.getValue().toString()));
        Long count5 = reportApiExp.countNotice(userId);
        if (count5 != 0) {
            module5.setCount(count5);
            workList.add(module5);
        }

        return workList;
    }
}
