package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.mapper.DictMapper;
import com.hbhb.cw.systemcenter.model.Dict;
import com.hbhb.cw.systemcenter.service.DictService;
import com.hbhb.cw.systemcenter.vo.DictVO;
import com.hbhb.cw.systemcenter.web.vo.DictResVO;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * @author dxk
 */
@Service
public class DictServiceImpl implements DictService {

    @Resource
    private DictMapper dictMapper;

    @Override
    @SuppressWarnings(value = {"rawtypes"})
    public PageResult<DictResVO> pageDictByCond(Long pageNum, Integer pageSize,
                                                String dictTypeName, String dictLabel) {
        PageRequest request = DefaultPageRequest.of(pageNum, pageSize);
        return dictMapper.selectPageByCond(dictTypeName, dictLabel, request);
    }

    @Override
    public List<DictVO> listDictByCond(String dictType, String dictCode) {
        List<DictVO> result = new ArrayList<>();
        List<Dict> dictList = dictMapper.selectListByCond(dictType, dictCode);
        if (!CollectionUtils.isEmpty(dictList)) {
            dictList.forEach(dict -> result.add(DictVO.builder()
                    .label(dict.getDictLabel())
                    .value(dict.getDictValue())
                    .build()));
        }
        return result;
    }

    @Override
    public Dict getDictInfo(Integer id) {
        return dictMapper.single(id);
    }

    @Override
    public void upsertDict(Dict dict) {
        dictMapper.upsertByTemplate(dict);
    }

    @Override
    public void deleteDict(Integer id) {
        dictMapper.deleteById(id);
    }
}
