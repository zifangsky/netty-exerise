package cn.zifangsky.netty.exercise.chapter12;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 数据解密传输
 *
 * @author zifangsky
 * @date 2020/10/26
 * @since 1.0.0
 */
public class SecureChatServerInitializer extends ChatServerInitializer {
    private final SslContext context;

    public SecureChatServerInitializer(ChannelGroup group, SslContext context) {
        super(group);
        this.context = context;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        //调用父类的initChannel()方法
        super.initChannel(ch);

        SSLEngine engine = context.newEngine(ch.alloc());
        engine.setUseClientMode(false);
        //将SslHandler添加到ChannelPipeline中
        ch.pipeline().addFirst(new SslHandler(engine));
    }
}
