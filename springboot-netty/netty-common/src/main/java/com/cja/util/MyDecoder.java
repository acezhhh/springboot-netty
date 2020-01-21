package com.cja.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by yuyufeng on 2017/8/28.
 */
public class MyDecoder extends ByteToMessageDecoder {
    private Class<?> genericClass;

    public MyDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() >= 4) {
            in.markReaderIndex();
            int length = in.readInt();
            //buf.getInt 会移动readIndex指针,如果数据不够会导致下一次包解析失败,所以需要先mark,在return之前再reset
            if (in.readableBytes() >= length) {
                byte[] data = new byte[length];
                in.readBytes(data);
                Object obj = ProtostuffUtil.deserializer(data, genericClass);
                out.add(obj);
            } else {
                in.resetReaderIndex();
                break;
            }
        }
    }
}
