package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.mapper.SysDictMapper;
import com.hbhb.cw.systemcenter.model.SysDict;
import com.hbhb.cw.systemcenter.service.SysDictService;
import com.hbhb.cw.systemcenter.vo.SysDictResVO;
import com.hbhb.cw.systemcenter.vo.SysDictVO;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * @author dxk
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Resource
    private SysDictMapper sysDictMapper;

    @Override
    public PageResult<SysDictResVO> pageDictByCond(Long pageNum, Integer pageSize,
                                                   String dictTypeName, String dictLabel) {
        PageRequest<SysDictResVO> request = DefaultPageRequest.of(pageNum, pageSize);
        return sysDictMapper.selectPageByCond(dictTypeName, dictLabel, request);
    }

    @Override
    public List<SysDictVO> listDictByCond(String dictType, String dictCode) {
        List<SysDictVO> result = new ArrayList<>();
        List<SysDict> list = sysDictMapper.selectListByCond(dictType, dictCode);
        for (SysDict sysDict : list) {
            result.add(SysDictVO.builder()
                    .label(sysDict.getDictLabel())
                    .value(sysDict.getDictValue())
                    .build());
        }
        return result;
    }

    @Override
    public SysDict getDictInfo(Integer id) {
        return sysDictMapper.single(id);
    }

    @Override
    public void upsertDict(SysDict sysDict) {
        sysDictMapper.upsertByTemplate(sysDict);
    }

    @Override
    public void deleteDict(Integer id) {
        sysDictMapper.deleteById(id);
    }
}
