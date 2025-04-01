package com.study.netty.file;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class FileNettyClientInitializer extends ChannelInitializer<SocketChannel> {
    SendFile sendFile;

    public FileNettyClientInitializer(SendFile fileUploadFile) {
        this.sendFile = fileUploadFile;
    }

    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline pipeline = sc.pipeline();
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(
                ClassResolvers
                        .weakCachingConcurrentResolver(null)));

        pipeline.addLast(new FileNettyClientHandler(sendFile));
    }
}
