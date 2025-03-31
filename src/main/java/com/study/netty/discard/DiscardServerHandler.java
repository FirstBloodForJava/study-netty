package com.study.netty.discard;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 继承 ChannelInboundHandlerAdapter,它是 ChannelInboundHandler 实现，提供了各种事件的处理方法
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 这里接收到的消息类型是 ByteBuf, 调用 release() 方法丢弃消息
        // ((ByteBuf) msg).release();

        ByteBuf in = (ByteBuf) msg;
        try {
            // 使用 telnet localhost 8080 就能看到这里打印的数据
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            System.out.println("释放数据");
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        // 打印捕获到的异常堆栈信息
        cause.printStackTrace();

        ctx.close();

    }
}
