package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.mapper.SysDictMapper;
import com.hbhb.cw.systemcenter.model.SysDict;
import com.hbhb.cw.systemcenter.service.SysDictService;
import com.hbhb.cw.systemcenter.vo.SysDictResVO;
import com.hbhb.cw.systemcenter.vo.SysDictVO;
import com.hbhb.springboot.web.view.Page;

import org.beetl.sql.core.engine.PageQuery;
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
    public Page<SysDictResVO> pageDictByCond(long pageNum, long pageSize,
                                             String dictTypeName, String dictLabel) {
        PageQuery<SysDictResVO> pageQuery = new PageQuery<>(pageNum, pageSize);
        sysDictMapper.selectPageByCond(pageQuery, dictTypeName, dictLabel);
        return new Page<>(pageQuery.getList(), pageQuery.getTotalRow());
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
