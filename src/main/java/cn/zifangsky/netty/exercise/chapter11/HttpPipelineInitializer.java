package cn.zifangsky.netty.exercise.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * Adding support for HTTP
 *
 * @author zifangsky
 * @date 2020/10/23
 * @since 1.0.0
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            //添加HttpResponseDecoder以处理来自服务器的响应
            pipeline.addLast("decoder", new HttpResponseDecoder());
            //添加HttpRequestEncoder以向服务器发送请求
            pipeline.addLast("encoder", new HttpRequestEncoder());
        } else {
            //添加HttpRequestDecoder以接收来自客户端的请求
            pipeline.addLast("decoder", new HttpRequestDecoder());
            //添加HttpResponseEncoder以向客户端发送响应
            pipeline.addLast("encoder", new HttpResponseEncoder());
        }
    }
}
