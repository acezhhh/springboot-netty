package com.cja.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ReconnectClientHandler extends ChannelInboundHandlerAdapter {

    private NettyClient client;

    public ReconnectClientHandler(NettyClient client) {
        this.client = client;
    }

    /**
     * 服务器断开后重新连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.start();
    }
}
