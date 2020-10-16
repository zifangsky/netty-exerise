package cn.zifangsky.netty.exercise.chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 * Discarding and releasing outbound data
 *
 * @author zifangsky
 * @date 2020/10/15
 * @since 1.0.0
 */
@Sharable
public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ReferenceCountUtil.release(msg);
        //通知ChannelPromise数据已经被处理了
        promise.setSuccess();
    }
}

