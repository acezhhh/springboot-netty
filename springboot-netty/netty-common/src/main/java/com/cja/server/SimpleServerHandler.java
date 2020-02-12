package com.cja.server;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cja.bean.RequsetBean;
import com.cja.bean.ResponseBean;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@ChannelHandler.Sharable
public class SimpleServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ApplicationContext applicationContext;

    @Qualifier("asyncServiceExecutor")
    @Autowired
    private Executor executor;

    /**
     * 读取数据并分发响应结果
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("SimpleServerHandler.channelRead");
        if (!(msg instanceof RequsetBean)) {
            System.out.println("The server received" + msg);
            return;
        }
        RequsetBean requsetBean = (RequsetBean) msg;
        CompletableFuture.supplyAsync(() -> {
            Object o = getResponse(requsetBean.getClassName(), requsetBean.getMethod(), requsetBean.getData());
            ResponseBean responseBean = new ResponseBean().toBuilder()
                    .resourceId(requsetBean.getResourceId())
                    .data(o)
                    .build();
            ctx.channel().writeAndFlush(responseBean);
            return null;
        }, executor).exceptionally(o -> new ResponseBean().toBuilder()
                .resourceId(requsetBean.getResourceId())
                .msg("远程调用异常！")
                .build());
    }

    /**
     * 根据反射调用对应的方法
     * @param className
     * @param methodName
     * @param args
     * @return
     */
    private Object getResponse(String className, String methodName, Object[] args) {
        try {
            //固定包名扫描
            Class<?> tClass = Class.forName("com.example.demo.service.impl."+className+"Impl");
            Object object = applicationContext.getBean(tClass);
            Class<? extends Object>[] paramClass = null;
            Method method = ReflectionUtils.findMethod(object.getClass(), methodName, paramClass);
            Object o = null;
            if (args == null) {
                o = ReflectionUtils.invokeMethod(method, object);
            } else {
                o = ReflectionUtils.invokeMethod(method, object,args);
            }
            return o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 本方法用于处理异常
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * 本方法用于向服务端发送信息
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String msg = "hello Client!";
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
        encoded.writeBytes(msg.getBytes());
        ctx.write(encoded);
        ctx.flush();
    }

}

