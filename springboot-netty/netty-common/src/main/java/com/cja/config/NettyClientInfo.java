package com.cja.config;

import com.cja.bean.RPCServiceInfo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@ConfigurationProperties("netty-clients")
@Data
public class NettyClientInfo {

    private List<RPCServiceInfo> services;

}
