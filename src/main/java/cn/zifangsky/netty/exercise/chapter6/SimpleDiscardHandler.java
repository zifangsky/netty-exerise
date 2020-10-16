package cn.zifangsky.netty.exercise.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Using SimpleChannelInboundHandler
 *
 * @author zifangsky
 * @date 2020/10/15
 * @since 1.0.0
 */
@ChannelHandler.Sharable
public class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //不需要任何显式的资源释放
    }
}
