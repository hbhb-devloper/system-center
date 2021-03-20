package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.cw.systemcenter.api.DictApi;
import com.hbhb.cw.systemcenter.model.SysDict;
import com.hbhb.cw.systemcenter.service.SysDictService;
import com.hbhb.cw.systemcenter.vo.DictIndexVO;
import com.hbhb.cw.systemcenter.vo.DictVO;
import com.hbhb.cw.systemcenter.web.vo.DictResVO;

import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@Tag(name = "字典")
@RestController
@RequestMapping("/dict")
@Slf4j
public class SysDictController implements DictApi {

    @Resource
    private SysDictService sysDictService;

    @Operation(summary = "分页获取字典列表")
    @GetMapping("/list")
    public PageResult<DictResVO> getDictList(
            @Parameter(description = "页码，默认为1") @RequestParam(required = false) Long pageNum,
            @Parameter(description = "每页数量，默认为10") @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "字典类型id") @RequestParam(required = false) Integer dictTypeId,
            @Parameter(description = "字典类型名称（模糊查询）") @RequestParam(required = false) String dictTypeName,
            @Parameter(description = "字典标签（模糊查询）") @RequestParam(required = false) String dictLabel) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return sysDictService.pageDictByCond(pageNum, pageSize, dictTypeId, dictTypeName, dictLabel);
    }

    @Operation(summary = "获取单条字典详情")
    @GetMapping("/{dictId}")
    public SysDict getDictInfo(@Parameter(description = "字典id") @PathVariable Integer dictId) {
        return sysDictService.getDictInfo(dictId);
    }

    @Operation(summary = "新增字典")
    @PostMapping("")
    public void saveDict(@Parameter(description = "字典实体") @RequestBody SysDict dict) {
        sysDictService.upsertDict(dict);
    }

    @Operation(summary = "更新字典")
    @PutMapping("")
    public void updateDict(@Parameter(description = "字典实体") @RequestBody SysDict dict) {
        sysDictService.upsertDict(dict);
    }

    @Operation(summary = "删除字典")
    @DeleteMapping("/{id}")
    public void deleteDict(@Parameter(description = "字典id") @PathVariable Integer id) {
        sysDictService.deleteDict(id);
    }

    @Operation(summary = "字典检索")
    @Override
    public List<DictVO> getDict(
            @Parameter(description = "字典类型") @RequestParam(required = false) String type,
            @Parameter(description = "字典编号") @RequestParam(required = false) String code) {
        return sysDictService.listDictByCond(type, code);
    }

    @Operation(summary = "获取字典索引")
    @GetMapping("/index")
    public List<DictIndexVO> getDictIndex() {
        return sysDictService.getDictIndex();
    }
}
