package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.cw.systemcenter.service.MqService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2020-09-12
 */
@Service
public class MqServiceImpl implements MqService {

    @Value("${cw.mq.topic-exchange}")
    private String topicExchange;
    @Value("${cw.mq.routing_key}")
    private String routingKey;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendMsg(Object object, String suffix) {
        rabbitTemplate.convertAndSend(topicExchange, routingKey + suffix, object);
    }
}
