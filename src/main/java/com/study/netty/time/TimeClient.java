package com.study.netty.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // Bootstrap 类似于 ServerBootstrap ，不同之处在于它适用于非服务器通道，如客户端或无连接通道。
            Bootstrap b = new Bootstrap();

            // 只指定一个EventLoopGroup，它将同时用作 boss组和 worker 组。boss组在客户端不使用
            b.group(workerGroup);

            // NioSocketChannel 被用来创建客户端通道，而不是NioServerSocketChannel(服务端)
            b.channel(NioSocketChannel.class);

            // 注意，这里我们没有像使用ServerBootstrap那样使用childOption()，因为客户端SocketChannel没有父类。
            b.option(ChannelOption.SO_KEEPALIVE, true);

            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // ch.pipeline().addLast(new TimeClientHandler(), new TimeDecoder());
                    ch.pipeline().addLast(new TimeClientHandler());
                }
            });

            // 连接服务端
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
