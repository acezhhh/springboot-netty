package com.cja.rpc;

import com.cja.bean.RequsetBean;
import com.cja.bean.ResponseBean;
import com.cja.client.NettyClient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


@NoArgsConstructor
@Component
@Data
public class RPCInvocationHandler implements InvocationHandler {

    private NettyClient nettyClient;

    private String serviceName;

    private Class<?> type;

    public RPCInvocationHandler(Class<?> type, String serviceName) {
        this.type = type;
        this.serviceName = serviceName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (nettyClient == null) {
            nettyClient = NettyRPCUtil.getNettyEntity(serviceName);
        }
        RequsetBean requsetBean = new RequsetBean().toBuilder()
                .className(type.getSimpleName())
                .method(method.getName())
                .data(args)
                .build();
        ResponseBean responseBean = nettyClient.sendSync(requsetBean);
        return responseBean.getData();
    }
}
