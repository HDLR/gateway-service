package com.dfjinxin.gateway.service.repsitory;

import com.alibaba.fastjson.JSON;
import com.dfjinxin.gateway.service.config.redis.CacheHelper;
import com.dfjinxin.gateway.service.service.GatewayDefineService;
import com.dfjinxin.gateway.service.vo.GatewayDefine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyRouteDefinitionRepository implements RouteDefinitionRepository {

    @Autowired
    GatewayDefineService gatewayDefineService;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            //从redis获取数据
            /**
             * 这里是否需要锁来限制并发？
             */
            List<GatewayDefine> gatewayDefineList = JSON.parseArray(CacheHelper.getCache("gateway_rotus").toString(),GatewayDefine.class);

            if(null == gatewayDefineList){
                gatewayDefineService.reset();
                gatewayDefineList = JSON.parseArray(CacheHelper.getCache("gateway_rotus").toString(),GatewayDefine.class);
            }

            Map<String, RouteDefinition> routes = new LinkedHashMap<String, RouteDefinition>();
            for (GatewayDefine gatewayDefine: gatewayDefineList) {
                RouteDefinition definition = new RouteDefinition();
                definition.setId(gatewayDefine.getId());
                definition.setUri(new URI(gatewayDefine.getUri()));
                List<PredicateDefinition> predicateDefinitions =
                        gatewayDefine.getPredicateDefinition();
                if (predicateDefinitions != null) {
                    definition.setPredicates(predicateDefinitions);
                }
                List<FilterDefinition> filterDefinitions = gatewayDefine.getFilterDefinition();
                if (filterDefinitions != null) {
                    definition.setFilters(filterDefinitions);
                }
                routes.put(definition.getId(), definition);
            }
            return Flux.fromIterable(routes.values());
        } catch (Exception e) {
            e.printStackTrace();
            return Flux.empty();
        }
    }


    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
       return null;
    }

}
