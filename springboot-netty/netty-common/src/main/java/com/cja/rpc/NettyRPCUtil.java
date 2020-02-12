package com.cja.rpc;


import com.cja.client.NettyClient;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 根据不同 RPCClient 的服务名称存多个NettyClient项目启动后连接服务端
 */
public class NettyRPCUtil {

    private static ConcurrentHashMap<String, NettyClient> resultSyncMap;

    static {
        resultSyncMap = new ConcurrentHashMap<>();
    }

    public static NettyClient getNettyEntity(String serviceName) {
        return resultSyncMap.get(serviceName);
    }

    public static void putNettyEntity(String serviceName,NettyClient nettyClient) {
        resultSyncMap.put(serviceName, nettyClient);
    }

    public static ConcurrentHashMap<String, NettyClient> getResultSyncMap() {
        return resultSyncMap;
    }

    public static void setResultSyncMap(ConcurrentHashMap<String, NettyClient> resultSyncMap) {
        NettyRPCUtil.resultSyncMap = resultSyncMap;
    }

    public static void clientService(){

    }

}
