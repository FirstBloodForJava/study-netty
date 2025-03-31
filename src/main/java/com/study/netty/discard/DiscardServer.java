package com.study.netty.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run(Class<? extends ChannelInboundHandlerAdapter > clazz) throws InterruptedException {
        // NioEventLoopGroup是一个处理I/O操作的多线程事件循环。Netty为不同类型的传输提供了各种EventLoopGroup实现。
        // 在本例中，我们将实现一个服务器端应用程序，因此将使用两个NioEventLoopGroup。
        // 第一个通常被称为 boss ，它接受传入的连接。
        // 第二个通常称为 worker ，一旦boss接受连接并将接受的连接注册到worker，它将处理已接受连接的流量。
        // 使用多少线程以及如何将它们映射到创建的通道取决于EventLoopGroup实现，甚至可以通过构造函数进行配置。
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // ServerBootstrap是一个设置服务器的助手类。您可以直接使用Channel设置服务器。但是，请注意，这是一个繁琐的过程，在大多数情况下不需要这样做。
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 在这里，指定使用 NioServerSocketChannel，该类用于实例化一个新 Channel 以接受传入的连接。
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 这里指定的处理程序将始终由新接受的通道计算。
                        // ChannelInitializer 是一个特殊的处理程序，用于帮助用户配置新通道。
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 继承 ChannelInboundHandlerAdapter 的新类
                            ch.pipeline().addLast(clazz.newInstance());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128) // 您还可以设置特定于Channel实现的参数。我们正在编写一个TCP/IP服务器，所以我们可以设置套接字选项，如tcpNoDelay和keepAlive。
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // option()用于 NioServerSocketChannel，它接受传入的连接。childOption()用于父ServerChannel接受的通道

            // Bind and start to accept incoming connections.
            // 绑定端口
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new DiscardServer(port).run(EchoServerHandler.class);
    }

}
