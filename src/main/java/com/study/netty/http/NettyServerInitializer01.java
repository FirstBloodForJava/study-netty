package com.study.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class NettyServerInitializer01 extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline pipeline = sc.pipeline();
        // 加入netty提供的处理器。语法：pipeline.addLast(定义处理器的名字,处理器);
        // HttpServerCodec: 对请求和响应进行编码、解码
        pipeline.addLast("HttpServerCodec", new HttpServerCodec());
        // 增加自定义处理器 NettyServerHandler ，用于实际处理请求，并给出响应
        pipeline.addLast("NettyServerHandler01", new NettyServerHandler01());
    }
}
