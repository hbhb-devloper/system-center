package com.hbhb.cw.systemcenter.service;

/**
 * @author xiaokang
 * @since 2020-09-12
 */
public interface MqService {

    /**
     * 发送消息到
     */
    void sendMsg(Object object, String suffix);
}
