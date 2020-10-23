package cn.zifangsky.netty.exercise.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Automatically compressing HTTP messages
 *
 * @author zifangsky
 * @date 2020/10/23
 * @since 1.0.0
 */
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpCompressionInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("codec", new HttpClientCodec());
            //添加HttpContentDecompressor以处理来自服务器的压缩内容
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        } else {
            pipeline.addLast("codec", new HttpServerCodec());
            //添加HttpContentCompressor来压缩数据（如果客户端支持它）
            pipeline.addLast("compressor", new HttpContentDecompressor());
        }
    }
}
