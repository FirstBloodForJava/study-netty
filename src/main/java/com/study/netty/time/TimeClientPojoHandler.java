package com.study.netty.time;

import io.netty.channel.ChannelHandlerContext;

public class TimeClientPojoHandler extends TimeClientHandler{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        UnixTime m = (UnixTime) msg;
        System.out.println(msg.getClass() + ": " + m);
        ctx.close();
    }
}
