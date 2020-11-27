package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.enums.TypeCode;
import com.hbhb.cw.systemcenter.mapper.DictMapper;
import com.hbhb.cw.systemcenter.model.Dict;
import com.hbhb.cw.systemcenter.service.DictService;
import com.hbhb.cw.systemcenter.vo.DictIndexVO;
import com.hbhb.cw.systemcenter.vo.DictVO;
import com.hbhb.cw.systemcenter.web.vo.DictResVO;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
