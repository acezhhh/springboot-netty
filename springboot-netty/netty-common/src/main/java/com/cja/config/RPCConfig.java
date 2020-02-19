package com.cja.config;

import com.cja.rpc.EnableRPC;
import com.cja.rpc.RPCClientRegistrar;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties

@ComponentScan("com.cja")
public class RPCConfig {

}
