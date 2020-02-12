package com.cja.config;

import com.cja.client.NettyClient;
import com.cja.rpc.NettyRPCUtil;
import com.cja.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务启动之后将开启netty连接
 */
@Component
@Slf4j
public class RPCServiceInit implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private NettyClientInfo nettyClientInfo;

    @Value("${netty-clients.enabled:false}")
    private Boolean clientEnabled;

    @Value("${netty-server.enabled:false}")
    private Boolean serverEnabled;

    @Autowired
    private NettyServer nettyServer;

    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (clientEnabled) {
            connectionServices();
        }

        if (serverEnabled) {
            CompletableFuture.supplyAsync(() -> {
                log.info("开启netty服务");
                nettyServer.start();
                return null;
            }, executor);
        }

    }

    private void connectionServices() {
        if (nettyClientInfo.getServices() == null) {
            return;
        }
        nettyClientInfo.getServices().forEach(rpcServiceInfo -> {
            String serviceName = rpcServiceInfo.getName();
            //添加netty启动服务
            NettyClient nettyClient = new NettyClient(rpcServiceInfo.getIp(), rpcServiceInfo.getPort());
            NettyRPCUtil.putNettyEntity(serviceName, nettyClient);
            CompletableFuture.supplyAsync(() -> {
                log.info("连接{}服务,地址：{}:{}", rpcServiceInfo.getName(), rpcServiceInfo.getIp(), rpcServiceInfo.getPort());
                NettyRPCUtil.getNettyEntity(serviceName).start();
                return null;
            }, executor);
        });
    }
}
