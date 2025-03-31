package com.study.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * ByteToMessageDecoder 是 ChannelInboundHandler 的一个实现，它可以很容易地处理碎片问题
 */
public class TimeDecoder extends ByteToMessageDecoder {

    // 每当接收到新数据时，ByteToMessageDecoder 使用内部维护的累积缓冲区调用 decode() 方法。
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) {
            // 当累积缓冲区中没有足够的数据时，decode() 可以决定不向out添加任何数据。ByteToMessageDecoder将在接收到更多数据时再次调用 decode()
            return;
        }
        // 如果 decode() 向out添加一个对象，则表示解码器成功解码了一条消息
        // ByteToMessageDecoder 将丢弃累积缓冲区的读部分
        out.add(in.readBytes(4));

    }
}
