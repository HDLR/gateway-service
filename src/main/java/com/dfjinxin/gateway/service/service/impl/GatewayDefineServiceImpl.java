package com.dfjinxin.gateway.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.dfjinxin.gateway.service.config.redis.CacheHelper;
import com.dfjinxin.gateway.service.mapper.GatewayDefineMapper;
import com.dfjinxin.gateway.service.service.GatewayDefineService;
import com.dfjinxin.gateway.service.vo.GatewayDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatewayDefineServiceImpl implements GatewayDefineService {

    @Autowired
    GatewayDefineMapper gatewayDefineMapper;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @Title reset
     * @Description: 更新redis缓存，同时触发监听，更新路由缓存
     * @Date: 2019/10/23 9:40
     */
    public void reset() throws Exception{
        List<GatewayDefine> gatewayDefineServiceAll = gatewayDefineMapper.findAll();
        CacheHelper.saveCache("gateway_rotus", JSON.toJSONString(gatewayDefineServiceAll));
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }


}
