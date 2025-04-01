package com.study.netty.file;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;

public class FileNettyClient {
    public static void connect(int port, String host,
                               final SendFile fileUploadFile) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new FileNettyClientInitializer(fileUploadFile));
            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        String path = "";
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        try {
            SendFile sendFile = new SendFile();
            File file = new File(path);
            String fileName = file.getName();
            sendFile.setFile(file);
            sendFile.setFileName(fileName);
            sendFile.setStart(0);
            connect(port, "127.0.0.1", sendFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
