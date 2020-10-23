package cn.zifangsky.netty.exercise.chapter11;

import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;

/**
 * Transferring file contents with FileRegion
 *
 * @author zifangsky
 * @date 2020/10/23
 * @since 1.0.0
 */
public class FileRegionWriteHandler extends ChannelInboundHandlerAdapter {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final File FILE_FROM_SOMEWHERE = new File("");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        File file = FILE_FROM_SOMEWHERE;
        Channel channel = CHANNEL_FROM_SOMEWHERE;

        FileInputStream in = new FileInputStream(file);
        //以该文件的完整长度创建一个新的DefaultFileRegion
        FileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());

        //发送该DefaultFileRegion，并注册一个ChannelFutureListener
        channel.writeAndFlush(region).addListener(future -> {
            if (!future.isSuccess()) {
                //处理失败情况
                Throwable cause = future.cause();
                // Do something
            }
        });
    }
}
