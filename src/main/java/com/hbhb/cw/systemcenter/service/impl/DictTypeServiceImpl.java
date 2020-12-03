package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.bean.SelectVO;
import com.hbhb.cw.systemcenter.mapper.DictTypeMapper;
import com.hbhb.cw.systemcenter.model.DictType;
import com.hbhb.cw.systemcenter.service.DictTypeService;

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
public class DictTypeServiceImpl implements DictTypeService {

    @Resource
    private DictTypeMapper dictTypeMapper;

    @Override
    public PageResult<DictType> pageDictTypeByCond(Long pageNum, Integer pageSize) {
        PageRequest request = DefaultPageRequest.of(pageNum, pageSize);
        return dictTypeMapper.selectPageByCond(request);
    }

    @Override
    public List<SelectVO> getAllDictType() {
        List<DictType> dictTypes = dictTypeMapper.createLambdaQuery()
                .select(DictType::getId, DictType::getTypeName);
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
    public DictType getDictTypeInfo(Integer dictTypeId) {
        return dictTypeMapper.single(dictTypeId);
    }

    @Override
    public void upsertDictType(DictType dictType) {
        dictTypeMapper.upsertByTemplate(dictType);
    }

    @Override
    public void deleteDictType(Integer id) {
        dictTypeMapper.deleteById(id);
    }
}
