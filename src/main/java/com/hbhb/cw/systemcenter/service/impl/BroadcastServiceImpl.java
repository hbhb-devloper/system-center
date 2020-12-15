package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.utils.JsonUtil;
import com.hbhb.cw.systemcenter.enums.BroadcastState;
import com.hbhb.cw.systemcenter.mapper.BroadcastMapper;
import com.hbhb.cw.systemcenter.model.Broadcast;
import com.hbhb.cw.systemcenter.service.BroadcastService;
import com.hbhb.cw.systemcenter.service.MqService;

import org.beetl.sql.core.page.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2020-09-09
 */
@Service
public class BroadcastServiceImpl implements BroadcastService {

    @Value("${cw.mq.key_suffix.broadcast}")
    private String keySuffix;

    @Resource
    private MqService mqService;
    @Resource
    private BroadcastMapper broadcastMapper;

    @Override
    public PageResult<Broadcast> pageBroadcast(Integer pageNum, Integer pageSize,
                                               String content, Byte state) {
        return broadcastMapper.createLambdaQuery()
                .andLike(Broadcast::getContent, content)
                .andEq(Broadcast::getState, state)
                .desc(Broadcast::getCreateTime)
                .page(pageNum, pageSize);
    }

    @Override
    public List<String> getPublishList() {
        List<Broadcast> broadcasts = broadcastMapper.createLambdaQuery()
                .andEq(Broadcast::getState, BroadcastState.ENABLE.value())
                .select();
        return Optional.ofNullable(broadcasts)
                .orElse(new ArrayList<>())
                .stream()
                .map(Broadcast::getContent)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upsertBroadcast(Broadcast broadcast) {
        broadcastMapper.upsertByTemplate(broadcast);
        // 发送最新公告列表至mq
        String msg = JsonUtil.convert2Str(this.getPublishList());
        if (msg != null) {
            mqService.sendMsg(msg, keySuffix);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBroadcast(Long id) {
        broadcastMapper.deleteById(id);
        // 发送最新公告列表至mq
        String msg = JsonUtil.convert2Str(this.getPublishList());
        if (msg != null) {
            mqService.sendMsg(msg, keySuffix);
        }
    }
}
