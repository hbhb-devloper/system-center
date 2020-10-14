package com.hbhb.cw.systemcenter.redis;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

/**
 * spring redis 工具类
 *
 * @author xiaokang
 **/
@Component
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class RedisHelper {

    @Resource
    public RedisTemplate redisTemplate;

    public <T> ValueOperations<String, T> set(String key, T value) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value);
        return operation;
    }

    public <T> ValueOperations<String, T> set(String key, T value, long timeout, TimeUnit timeUnit) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value, timeout, timeUnit);
        return operation;
    }

    public <T> T get(String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void delete(Collection collection) {
        redisTemplate.delete(collection);
    }

    public <T> ListOperations<String, T> setList(String key, List<T> dataList) {
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList) {
            for (T t : dataList) {
                listOperation.leftPush(key, t);
            }
        }
        return listOperation;
    }

    public <T> List<T> getList(String key) {
        List<T> dataList = new ArrayList<>();
        ListOperations<String, T> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);
        if (!StringUtils.isEmpty(size)) {
            for (int i = 0; i < size; i++) {
                dataList.add(listOperation.index(key, i));
            }
        }
        return dataList;
    }

    public <T> BoundSetOperations<String, T> setSet(String key, Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        for (T t : dataSet) {
            setOperation.add(t);
        }
        return setOperation;
    }

    public <T> Set<T> getSet(String key) {
        BoundSetOperations<String, T> operation = redisTemplate.boundSetOps(key);
        return operation.members();
    }

    public <T> HashOperations<String, String, T> setMap(String key, Map<String, T> dataMap) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (dataMap != null) {
            for (Map.Entry<String, T> entry : dataMap.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
        }
        return hashOperations;
    }

    public <T> Map<String, T> getMap(String key) {
        return (Map<String, T>) redisTemplate.opsForHash().entries(key);
    }

    public Collection<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
