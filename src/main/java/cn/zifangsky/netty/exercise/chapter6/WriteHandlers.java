package cn.zifangsky.netty.exercise.chapter6;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DummyChannelPipeline;
import io.netty.util.CharsetUtil;

import static io.netty.channel.DummyChannelHandlerContext.DUMMY_INSTANCE;

/**
 * f
 *
 * @author zifangsky
 * @date 2020/10/16
 * @since 1.0.0
 */
public class WriteHandlers {
    private static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DUMMY_INSTANCE;
    private static final ChannelPipeline CHANNEL_PIPELINE_FROM_SOMEWHERE = DummyChannelPipeline.DUMMY_INSTANCE;


    /**
     * Calling Channel write()
     */
    private void writeByChannel(){
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;
        Channel channel = ctx.channel();

        channel.write(Unpooled.copiedBuffer("Netty in action", CharsetUtil.UTF_8));
    }

    /**
     * Calling ChannelPipeline write()
     */
    private void writeByChannelPipeline(){
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;
        ChannelPipeline pipeline = ctx.pipeline();

        pipeline.write(Unpooled.copiedBuffer("Netty in action", CharsetUtil.UTF_8));
    }

    /**
     * Calling ChannelHandlerContext write()
     */
    private void writeByChannelHandlerContext(){
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;

        ctx.write(Unpooled.copiedBuffer("Netty in action", CharsetUtil.UTF_8));
    }






}
