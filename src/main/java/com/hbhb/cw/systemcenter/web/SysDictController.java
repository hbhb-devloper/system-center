package com.hbhb.cw.systemcenter.web;

import com.hbhb.cw.systemcenter.api.SysDictApi;
import com.hbhb.cw.systemcenter.enums.DictCode;
import com.hbhb.cw.systemcenter.enums.DictType;
import com.hbhb.cw.systemcenter.model.SysDict;
import com.hbhb.cw.systemcenter.service.SysDictService;
import com.hbhb.cw.systemcenter.vo.SysDictResVO;
import com.hbhb.cw.systemcenter.vo.SysDictVO;
import com.hbhb.springboot.webflux.view.Page;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaokang
 * @since 2020-07-15
 */
@Api(tags = "字典相关")
@RestController
@RequestMapping("/dict")
@Slf4j
public class SysDictController implements SysDictApi {

    @Resource
    private SysDictService sysDictService;

    @Override
    public Page<SysDictResVO> getDictList(Long pageNum, Long pageSize,
                                          String dictTypeName, String dictLabel) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return sysDictService.pageDictByCond(pageNum, pageSize, dictTypeName, dictLabel);
    }

    @Override
    public SysDict getDictInfo(Integer id) {
        return sysDictService.getDictInfo(id);
    }

    @Override
    public void saveDict(SysDict sysDict) {
        sysDictService.upsertDict(sysDict);
    }

    @Override
    public void updateDict(SysDict sysDict) {
        sysDictService.upsertDict(sysDict);
    }

    @Override
    public void deleteDict(Integer id) {
        sysDictService.deleteDict(id);
    }

    @Override
    public List<SysDictVO> getModuleList() {
        return sysDictService.listDictByCond(DictType.MODULE.value(), null);
    }

    @Override
    public List<SysDictVO> getProjectStatus() {
        return sysDictService.listDictByCond(DictType.BUDGET.value(), DictCode.BUDGET_PROJECT_STATUS.value());
    }

    @Override
    public List<SysDictVO> getProjectOrigin() {
        return sysDictService.listDictByCond(DictType.BUDGET.value(), DictCode.BUDGET_PROJECT_ORIGIN.value());
    }

    @Override
    public List<SysDictVO> getProjectVatRate() {
        return sysDictService.listDictByCond(DictType.BUDGET.value(), DictCode.BUDGET_PROJECT_VAT_RATES.value());
    }
}
