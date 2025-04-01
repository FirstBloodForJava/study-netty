package com.study.netty.file;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.RandomAccessFile;

public class FileNettyServerHandler extends SimpleChannelInboundHandler {
    private int readLength;
    private int start = 0;
    private String fileDir = "";

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof SendFile) {
            SendFile sendFile = (SendFile) msg;
            byte[] bytes = sendFile.getBytes();
            readLength = sendFile.getEnd();
            String fileName = sendFile.getFileName();
            String path = fileDir + File.separator + fileName;
            File file = new File(path);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);
            start = start + readLength;
            if (readLength > 0) {
                ctx.writeAndFlush(start);
                randomAccessFile.close();
            } else {
                ctx.flush();
                ctx.close();
            }

        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.flush();
        ctx.close();
    }
}
