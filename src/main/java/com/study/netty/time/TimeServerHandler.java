package com.study.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 重写事件方法
     * @param ctx
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // 定义缓冲区
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        // 发送消息 因为异步，所有不能直接调用 ctx.close()方法
        final ChannelFuture f = ctx.writeAndFlush(time);

        f.addListener(new ChannelFutureListener() {
            // 消息发送完毕后注册回调
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                ctx.close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
