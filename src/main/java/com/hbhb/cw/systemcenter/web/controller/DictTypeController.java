package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.core.bean.BeanConverter;
import com.hbhb.core.bean.SelectVO;
import com.hbhb.cw.systemcenter.model.DictType;
import com.hbhb.cw.systemcenter.service.DictTypeService;
import com.hbhb.cw.systemcenter.web.vo.DictTypeVO;

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
@Tag(name = "字典类型")
@RestController
@RequestMapping("/dict/type")
@Slf4j
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    @Operation(summary = "分页获取字典类型列表", description = "管理页面用")
    @GetMapping("/page")
    public PageResult<DictType> getDictTypePage(
            @Parameter(description = "页码，默认为1") @RequestParam(required = false) Long pageNum,
            @Parameter(description = "每页数量，默认为10") @RequestParam(required = false) Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return dictTypeService.pageDictTypeByCond(pageNum, pageSize);
    }

    @Operation(summary = "获取字典类型列表", description = "下拉框用")
    @GetMapping("/list")
    public List<SelectVO> getDictTypeList() {
        return dictTypeService.getAllDictType();
    }

    @Operation(summary = "获取字典类型详情")
    @GetMapping("/{dictTypeId}")
    public DictType getDictTypeInfo(
            @Parameter(description = "字典类型id") @PathVariable Integer dictTypeId) {
        return dictTypeService.getDictTypeInfo(dictTypeId);
    }

    @Operation(summary = "新增字典类型")
    @PostMapping("")
    public void saveDict(@Parameter(description = "字典类型实体") @RequestBody DictTypeVO vo) {
        DictType dictType = new DictType();
        BeanConverter.copyProp(vo, dictType);
        dictTypeService.upsertDictType(dictType);
    }

    @Operation(summary = "更新字典类型")
    @PutMapping("")
    public void updateDict(@Parameter(description = "字典类型实体") @RequestBody DictTypeVO vo) {
        DictType dictType = new DictType();
        BeanConverter.copyProp(vo, dictType);
        dictTypeService.upsertDictType(dictType);
    }

    @Operation(summary = "删除字典类型")
    @DeleteMapping("/{id}")
    public void deleteDict(@Parameter(description = "字典类型id") @PathVariable Integer id) {
        dictTypeService.deleteDictType(id);
    }
}
