package com.dfjinxin.gateway.service.mapper;

import com.dfjinxin.gateway.service.vo.GatewayDefine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GatewayDefineMapper {
    /**
     * 从数据库获取动态配置；
     * 可以根据业务需要进行sql修改
     */
    @Select("select * from gateway_define")
    List<GatewayDefine> findAll();
}
