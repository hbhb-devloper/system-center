package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.mapper.DictTypeMapper;
import com.hbhb.cw.systemcenter.model.DictType;
import com.hbhb.cw.systemcenter.service.DictTypeService;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dxk
 */
@Service
public class DictTypeServiceImpl implements DictTypeService {

    @Resource
    private DictTypeMapper dictTypeMapper;

    @Override
    @SuppressWarnings(value = {"rawtypes"})
    public PageResult<DictType> pageDictTypeByCond(Long pageNum, Integer pageSize) {
        PageRequest request = DefaultPageRequest.of(pageNum, pageSize);
        return dictTypeMapper.selectPageByCond(request);
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
