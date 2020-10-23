package cn.zifangsky.netty.exercise.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * Adding SSL/TLS support
 *
 * @author zifangsky
 * @date 2020/10/23
 * @since 1.0.0
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {
    /**
     * 传入要使用的SslContext
     */
    private final SslContext context;
    /**
     * 如果设置为true，第一个写入的消息将不会被加密（客户端应该设置为true）
     */
    private final boolean startTls;

    public SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        //对于每个SslHandler实例，都使用Channel的ByteBuf-Allocator从SslContext获取一个新的SSLEngine
        SSLEngine engine = context.newEngine(ch.alloc());

        //将SslHandler作为第一个ChannelHandler添加到ChannelPipeline中
        ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }
}