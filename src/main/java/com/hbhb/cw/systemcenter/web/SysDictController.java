package com.hbhb.cw.systemcenter.web;

import com.hbhb.cw.systemcenter.api.SysDictApi;
import com.hbhb.cw.systemcenter.enums.DictCode;
import com.hbhb.cw.systemcenter.enums.DictType;
import com.hbhb.cw.systemcenter.model.SysDict;
import com.hbhb.cw.systemcenter.service.SysDictService;
import com.hbhb.cw.systemcenter.vo.SysDictResVO;
import com.hbhb.cw.systemcenter.vo.SysDictVO;

import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaokang
 * @since 2020-07-15
 */
@Tag(name = "字典相关")
@RestController
@RequestMapping("/dict")
@Slf4j
public class SysDictController implements SysDictApi {

    @Resource
    private SysDictService sysDictService;

    @Operation(summary = "分页获取字典列表")
    @Override
    public PageResult<SysDictResVO> getDictList(@Parameter(description = "页码，默认为1") Long pageNum,
                                                @Parameter(description = "每页数量，默认为10") Integer pageSize,
                                                @Parameter(description = "字典类型名称（模糊查询）") String dictTypeName,
                                                @Parameter(description = "字典标签（模糊查询）") String dictLabel) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return sysDictService.pageDictByCond(pageNum, pageSize, dictTypeName, dictLabel);
    }

    @Operation(summary = "获取单条字典详情")
    @Override
    public SysDict getDictInfo(@Parameter(description = "字典id") Integer id) {
        return sysDictService.getDictInfo(id);
    }

    @Operation(summary = "新增字典")
    @Override
    public void saveDict(@Parameter(description = "字典实体") SysDict sysDict) {
        sysDictService.upsertDict(sysDict);
    }

    @Operation(summary = "更新字典")
    @Override
    public void updateDict(@Parameter(description = "字典实体") SysDict sysDict) {
        sysDictService.upsertDict(sysDict);
    }

    @Operation(summary = "删除字典")
    @Override
    public void deleteDict(@Parameter(description = "字典id") Integer id) {
        sysDictService.deleteDict(id);
    }

    @Operation(summary = "功能模块字典列表")
    @Override
    public List<SysDictVO> getModuleList() {
        return sysDictService.listDictByCond(DictType.MODULE.value(), null);
    }

    @Operation(summary = "项目签报-流程状态")
    @Override
    public List<SysDictVO> getProjectStatus() {
        return sysDictService.listDictByCond(DictType.BUDGET.value(), DictCode.BUDGET_PROJECT_STATUS.value());
    }

    @Operation(summary = "项目签报-项目来源")
    @Override
    public List<SysDictVO> getProjectOrigin() {
        return sysDictService.listDictByCond(DictType.BUDGET.value(), DictCode.BUDGET_PROJECT_ORIGIN.value());
    }

    @Operation(summary = "项目签报-增值税率")
    @Override
    public List<SysDictVO> getProjectVatRate() {
        return sysDictService.listDictByCond(DictType.BUDGET.value(), DictCode.BUDGET_PROJECT_VAT_RATES.value());
    }

    @Override
    public List<SysDictVO> getCompensationSate() {
        return null;
    }
}
