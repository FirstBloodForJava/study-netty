package com.study.netty.discard;

import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends DiscardServerHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //
        ctx.write(msg);
        //
        ctx.flush();

    }

}
