package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.cw.systemcenter.mapper.SysDictTypeMapper;
import com.hbhb.cw.systemcenter.model.SysDictType;
import com.hbhb.cw.systemcenter.service.SysDictTypeService;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author dxk
 */
@Service
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class SysDictTypeServiceImpl implements SysDictTypeService {

    @Resource
    private SysDictTypeMapper sysDictTypeMapper;

    @Override
    public PageResult<SysDictType> pageDictTypeByCond(Long pageNum, Integer pageSize) {
        PageRequest request = DefaultPageRequest.of(pageNum, pageSize);
        return sysDictTypeMapper.selectPageByCond(request);
    }

    @Override
    public List<SelectVO> getAllDictType() {
        List<SysDictType> dictTypes = sysDictTypeMapper.createLambdaQuery()
                .select(SysDictType::getId, SysDictType::getTypeName);
        return Optional.ofNullable(dictTypes)
                .orElse(new ArrayList<>())
                .stream()
                .map(dictType -> SelectVO.builder()
                        .id((long) dictType.getId())
                        .label(dictType.getTypeName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public SysDictType getDictTypeInfo(Integer dictTypeId) {
        return sysDictTypeMapper.single(dictTypeId);
    }

    @Override
    public void upsertDictType(SysDictType dictType) {
        sysDictTypeMapper.upsertByTemplate(dictType);
    }

    @Override
    public void deleteDictType(Integer id) {
        sysDictTypeMapper.deleteById(id);
    }
}
