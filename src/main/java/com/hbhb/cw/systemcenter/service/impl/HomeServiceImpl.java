package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.enums.DictCode;
import com.hbhb.cw.systemcenter.enums.TypeCode;
import com.hbhb.cw.systemcenter.service.HomeService;
import com.hbhb.cw.systemcenter.service.SysDictService;
import com.hbhb.cw.systemcenter.vo.DictVO;
import com.hbhb.cw.systemcenter.web.vo.HomeModuleVO;
import com.hbhb.cw.systemcenter.web.vo.HomeTodoVO;

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

    //    @Resource
//    private BudgetProjectNoticeService budgetNoticeService;
//    @Resource
//    private FundInvoiceNoticeService fundNoticeService;
    @Resource
    private SysDictService sysDictService;

    @Override
    public List<HomeModuleVO> getModuleList(Integer userId) {
        List<HomeModuleVO> workList = new ArrayList<>();
        List<DictVO> dictList = sysDictService.listDictByCond(
                TypeCode.MODULE.value(), DictCode.MODULE.value());
        Map<String, String> dictMap = dictList.stream().collect(
                Collectors.toMap(DictVO::getValue, DictVO::getLabel));
//        // 预算提醒统计
//        HomeModuleVO vo1 = new HomeModuleVO();
//        int budgetNoticeAccount = budgetNoticeService.getNoticeAccount(user.getId());
//        vo1.setModule(Module.MODULE_BUDGET.getValue());
//        vo1.setModuleName(dictMap.get(Module.MODULE_BUDGET.getValue().toString()));
//        vo1.setCount(budgetNoticeAccount);
//        workList.add(vo1);
//
//        // 客户资金提醒统计
//        HomeModuleVO vo2 = new HomeModuleVO();
//        int fundNoticeAccount = fundNoticeService.getNoticeAccount(user.getId());
//        vo2.setModule(Module.MODULE_INVOICE.getValue());
//        vo2.setModuleName(dictMap.get(Module.MODULE_INVOICE.getValue().toString()));
//        vo2.setCount(fundNoticeAccount);
//        workList.add(vo2);

        // todo 迁改预警提醒
//        List<String> roleName = roleUserService.getRoleName(user.getId());
//        if (roleName.contains("迁改预警负责人")) {
//            int warnCount = relocationApi.getWarnCount(user.getUnitId());
//            WorkBenchResVO work2 = new WorkBenchResVO();
//            work2.setModule(Module.MODULE_RELOCATION.getValue());
//            work2.setModuleName(moduleMap.get(Module.MODULE_RELOCATION.getValue().toString()));
//            work2.setCount(warnCount);
//            workList.add(work2);
//        }
        return workList;
    }

    @Override
    public List<HomeTodoVO> getTodoList(Integer module, Integer userId) {
        // 预算模块代办提醒
//        BudgetProjectNoticeVO bpNoticeVo = new BudgetProjectNoticeVO();
//        bpNoticeVo.setUserId(user.getId());
//        if (module.equals(Module.MODULE_BUDGET.getValue())) {
//            return budgetNoticeService.getBudgetNoticeList(user);
//            // 客户资金模块代办提醒
//        } else if (module.equals(Module.MODULE_INVOICE.getValue())) {
//            return fundNoticeService.getInvoiceNotice(user);
//        }
        return null;
    }
}
