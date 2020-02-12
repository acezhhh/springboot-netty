package com.cja.server;

import com.cja.bean.RequsetBean;
import com.cja.bean.ResponseBean;
import com.cja.util.MyDecoder;
import com.cja.util.MyEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class NettyServer {
    /**
     * boss 线程组用于处理连接工作
     */
    private EventLoopGroup acceptor = new NioEventLoopGroup();
    ;
    /**
     * work 线程组用于数据处理
     */
    private EventLoopGroup worker = new NioEventLoopGroup();
    @Value("${netty.port:0000}")
    private Integer port;

    @Autowired
    private SimpleServerHandler simpleServerHandler;

    /**
     * 启动Netty Server
     *
     * @throws InterruptedException
     */
    public void start() {
        try {
            //1、创建启动类
            ServerBootstrap bootstrap = new ServerBootstrap();
            //2、配置启动参数等
            /**设置循环线程组，前者用于处理客户端连接事件，后者用于处理网络IO(server使用两个参数这个)
             *public ServerBootstrap group(EventLoopGroup group)
             *public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup)
             */
            bootstrap.group(acceptor, worker);
            /**设置选项
             * 参数：Socket的标准参数（key，value），可自行百度
             * eg:
             * bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
             *bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
             * */
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 1024)// 配置TCP参数
                    .option(ChannelOption.SO_BACKLOG, 1024) // 设置tcp缓冲区
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024) // 设置发送缓冲大小
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024); // 这是接收缓冲大小

            //用于构造socketchannel工厂
            bootstrap.channel(NioServerSocketChannel.class);
            /**
             * 传入自定义客户端Handle
             */
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new MyDecoder(RequsetBean.class),
                            new MyEncoder(ResponseBean.class),
                            simpleServerHandler
                            );
                }
            });

            // 绑定端口，开始接收进来的连接
            ChannelFuture f = bootstrap.bind(port).sync();

            // 等待服务器 socket 关闭 。
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            acceptor.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public void destory() throws InterruptedException {
        acceptor.shutdownGracefully().sync();
        worker.shutdownGracefully().sync();
        log.info("关闭Netty");
    }
}
