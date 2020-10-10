package cn.zifangsky.netty.exercise.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 负责处理的{@link ChannelHandler}实例
 *
 * @author zifangsky
 * @date 2020/9/18
 * @since 1.0.0
 */
@Slf4j
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对接收消息的处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;

        //1. 打印接收到的消息
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        //2. 将接收到的消息重新发送给发送者
        ctx.write(in);
        ctx.flush();
    }

    /**
     * Read Complete的处理
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 对异常的处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //1. 打印日志
        log.error("数据处理过程中发生异常！", cause);
        //2. 关闭Channel
        ctx.close();
    }
}