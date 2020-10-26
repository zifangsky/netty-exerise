package cn.zifangsky.netty.exercise.chapter12;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.CharsetUtil;

/**
 * 处理文本帧
 *
 * @author zifangsky
 * @date 2020/10/26
 * @since 1.0.0
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    /**
     * 重写{@link io.netty.channel.ChannelInboundHandlerAdapter#userEventTriggered(ChannelHandlerContext, Object)}方法以处理自定义事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            //如果该事件表示握手成功，则从该ChannelPipeline中移除HttpRequestHandler，因为将不会接收到任何HTTP消息了
            ctx.pipeline().remove(HttpRequestHandler.class);

            //通知所有已经连接的WebSocket客户端新的客户端已经连接上了
            this.group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel().id() + " joined!"));
            //将新的WebSocket Channel添加到ChannelGroup中，以便它可以接收到所有消息
            this.group.add(ctx.channel());
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //增加消息的引用计数，并将它写到ChannelGroup 中所有已经连接的客户端
        String resMsg = String.format("[%s] say: %s", ctx.channel().id(), msg.text());
        msg = msg.replace(Unpooled.copiedBuffer(resMsg, CharsetUtil.UTF_8));

        this.group.writeAndFlush(msg.retain());
    }
}
