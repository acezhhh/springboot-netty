//package com.example.demo.config;
//
//import com.cja.bean.ResponseBean;
//import com.cja.bean.ResultSync;
//import com.cja.util.ProtostuffUtil;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//public class SimpleClientHandler extends ChannelInboundHandlerAdapter {
//
//    private ConcurrentHashMap<String, ResultSync> resultSyncMap = new ConcurrentHashMap<>();
//
//    /**
//     * 本方法用于接收服务端发送过来的消息
//     *
//     * @param ctx
//     * @param msg
//     * @throws Exception
//     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
////        ResponseBean result = ProtostuffUtil.deserializer(toByteArray(msg), ResponseBean.class);
//        ResponseBean result = (ResponseBean) msg;
//        String resourceId = result.getResourceId();
//        if (!resultSyncMap.containsKey(resourceId)) {
//            log.error("系统异常！");
//            return;
//        }
//        ResultSync resultSync = resultSyncMap.get(resourceId);
//        resultSync.setResponseBean(result);
//        resultSyncMap.put(resourceId, resultSync);
//        resultSync.getCountDownLatch().countDown();
//    }
//
//
//    public byte[] toByteArray (Object obj) {
//        byte[] bytes = null;
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(bos);
//            oos.writeObject(obj);
//            oos.flush();
//            bytes = bos.toByteArray ();
//            oos.close();
//            bos.close();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return bytes;
//    }
//
//    /**
//     * 本方法用于处理异常
//     *
//     * @param ctx
//     * @param cause
//     * @throws Exception
//     */
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        // 当出现异常就关闭连接
//        cause.printStackTrace();
//        ctx.close();
//    }
//
//
//    /**
//     * 本方法用于向服务端发送信息
//     *
//     * @param ctx
//     * @throws Exception
//     */
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        String msg = "hello Server!";
//        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
//        encoded.writeBytes(msg.getBytes());
//        ctx.write(encoded);
//        ctx.flush();
//    }
//
//    public void addResultSync(String key, ResultSync resultSync) {
//        resultSyncMap.put(key, resultSync);
//    }
//
//    public ResponseBean getResponseBean(String resourceId) {
//        if (!resultSyncMap.containsKey(resourceId)) {
//            return new ResponseBean();
//        }
//        ResultSync resultSync = resultSyncMap.get(resourceId);
//        resultSyncMap.remove(resourceId);
//        return resultSync.getResponseBean();
//    }
//
//    public ConcurrentHashMap<String, ResultSync> getResultSyncMap() {
//        return resultSyncMap;
//    }
//
//    public void setResultSyncMap(ConcurrentHashMap<String, ResultSync> resultSyncMap) {
//        this.resultSyncMap = resultSyncMap;
//    }
//}
