//package com.example.demo.config;
//
//import com.cja.bean.RequsetBean;
//import com.cja.bean.ResponseBean;
//import com.cja.bean.ResultSync;
//import com.cja.util.JdkDecoder;
//import com.cja.util.JdkEncoder;
//import com.cja.util.MyDecoder;
//import com.cja.util.MyEncoder;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//import java.util.concurrent.*;
//
//
//@Configuration
//@Component
//@Slf4j
//public class NettyClient {
//
//    @Qualifier("asyncServiceExecutor")
//    @Autowired
//    private Executor executor;
//
//    @Value("${netty.port}")
//    private Integer port;
//
//    private ChannelFuture channelFuture;
//
//    private SimpleClientHandler simpleClientHandler = new SimpleClientHandler();
//
//    public void start() {
//        EventLoopGroup worker = new NioEventLoopGroup();
//        try {
//            Bootstrap b = new Bootstrap();
//            /**
//             *EventLoop的组
//             */
//            b.group(worker);
//            /**
//             * 用于构造socketchannel工厂
//             */
//            b.channel(NioSocketChannel.class);
//            /**设置选项
//             * 参数：Socket的标准参数（key，value），可自行百度
//             保持呼吸，不要断气！
//             * */
//            b.option(ChannelOption.SO_KEEPALIVE, true)
//                    .option(ChannelOption.SO_BACKLOG, 1024)// 配置TCP参数
//                    .option(ChannelOption.SO_BACKLOG, 1024) // 设置tcp缓冲区
//                    .option(ChannelOption.SO_SNDBUF, 32 * 1024) // 设置发送缓冲大小
//                    .option(ChannelOption.SO_RCVBUF, 32 * 1024); // 这是接收缓冲大小
//
//            /**
//             * 自定义客户端Handle（客户端在这里搞事情）
//             */
//            b.handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                public void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast(
//                            new MyDecoder(ResponseBean.class),
//                            new MyEncoder(RequsetBean.class),
//                            simpleClientHandler);
//                }
//            });
//            /** 开启客户端监听*/
//            channelFuture = b.connect("127.0.0.1", port).sync();
//            /**等待数据直到客户端关闭*/
//
////            f.channel().writeAndFlush(Unpooled.copiedBuffer("test", CharsetUtil.UTF_8));
////            channelFuture.channel().writeAndFlush(requsetBean);
//            channelFuture.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            worker.shutdownGracefully();
//        }
//    }
//
//    public ResponseBean sendSync(RequsetBean requsetBean) {
//        String resourceId = UUID.randomUUID().toString();
//        try {
//            //设置返回值
//            CountDownLatch countDownLatch = new CountDownLatch(1);
//            ResultSync resultSync = new ResultSync().toBuilder().countDownLatch(countDownLatch).build();
//            simpleClientHandler.addResultSync(resourceId, resultSync);
//
//            //发送数据
//            requsetBean.setResourceId(resourceId);
//            channelFuture.channel().writeAndFlush(requsetBean);
//            //超时时间
//            countDownLatch.await(10, TimeUnit.SECONDS);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return getResult(resourceId);
//    }
//
//    private ResponseBean getResult(String resourceId) {
//        ResponseBean result = simpleClientHandler.getResponseBean(resourceId);
//        if (result == null) {
//            return new ResponseBean().toBuilder().msg("系统异常，请重试！").code("500").build();
//        }
//        return result;
//    }
//
//}
