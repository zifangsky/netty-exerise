package cn.zifangsky.netty.exercise.chapter11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Handling line-delimited frames
 *
 * @author zifangsky
 * @date 2020/10/23
 * @since 1.0.0
 */
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //该LineBasedFrameDecoder将提取的帧转发给下一个ChannelInboundHandler
        pipeline.addLast(new LineBasedFrameDecoder(64 * 1024));
        //添加FrameHandler以接收帧
        pipeline.addLast(new FrameHandler());
    }

    public static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            // Do something with the data extracted from the frame
        }
    }
}
