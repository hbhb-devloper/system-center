package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.enums.TypeCode;
import com.hbhb.cw.systemcenter.mapper.SysDictMapper;
import com.hbhb.cw.systemcenter.model.SysDict;
import com.hbhb.cw.systemcenter.service.SysDictService;
import com.hbhb.cw.systemcenter.vo.DictIndexVO;
import com.hbhb.cw.systemcenter.vo.DictVO;
import com.hbhb.cw.systemcenter.web.vo.DictResVO;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author dxk
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Resource
    private SysDictMapper sysDictMapper;

    @Override
    @SuppressWarnings(value = {"rawtypes"})
    public PageResult<DictResVO> pageDictByCond(Long pageNum, Integer pageSize,
                                                Integer dictTypeId, String dictTypeName, String dictLabel) {
        PageRequest request = DefaultPageRequest.of(pageNum, pageSize);
        return sysDictMapper.selectPageByCond(dictTypeId, dictTypeName, dictLabel, request);
    }

    @Override
    public List<DictVO> listDictByCond(String dictType, String dictCode) {
        List<SysDict> dictList = sysDictMapper.selectListByCond(dictType, dictCode);
        return Optional.ofNullable(dictList)
                .orElse(new ArrayList<>())
                .stream()
                .map(dict -> DictVO.builder()
                        .label(dict.getDictLabel())
                        .value(dict.getDictValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public SysDict getDictInfo(Integer id) {
        return sysDictMapper.single(id);
    }

    @Override
    public void upsertDict(SysDict dict) {
        sysDictMapper.upsertByTemplate(dict);
    }

    @Override
    public void deleteDict(Integer id) {
        sysDictMapper.deleteById(id);
    }

    @Override
    public List<DictIndexVO> getDictIndex() {
        List<DictIndexVO> list = new ArrayList<>();
        for (TypeCode typeCode : TypeCode.values()) {
            list.add(DictIndexVO.builder()
                    .type(typeCode.value())
                    .code(getDescGroup(typeCode.value()))
                    .build());
        }
        return list;
    }

    /**
     * 按前缀名获取DictCode属性组
     */
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    private Map<String, String> getDescGroup(String type) {
        Map<String, String> map = new HashMap<>(8);
        try {
            Class<Enum> em = (Class<Enum>) Class.forName("com.hbhb.cw.systemcenter.enums.DictCode");
            Method toValue = em.getMethod("value");
            Method toDesc = em.getMethod("desc");
            //得到enum的所有实例
            Object[] constants = em.getEnumConstants();
            Field[] fields = em.getFields();
            for (int i = 0; i < fields.length; i++) {
                if (type.equalsIgnoreCase(fields[i].getName().split("_")[0])) {
                    map.put((String) toValue.invoke(constants[i]), (String) toDesc.invoke(constants[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
