package cn.zifangsky.netty.exercise.chapter8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

import java.net.InetSocketAddress;

/**
 * Bootstrapping and using ChannelInitializer
 *
 * @author zifangsky
 * @date 2020/10/19
 * @since 1.0.0
 */
public class BootstrapWithInitializer {

    public static void main(String[] args) throws InterruptedException {
        BootstrapWithInitializer server = new BootstrapWithInitializer();
        server.bootstrap();
    }

    /**
     * Bootstrapping a server
     */
    public void bootstrap() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(8080))
                //注册一个ChannelInitializerImpl的实例来设置ChannelPipeline
                .childHandler(new ChannelInitializerImpl());

        bootstrap.bind().sync();
    }


    final class ChannelInitializerImpl extends ChannelInitializer<Channel> {

        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();

            //将所需的ChannelHandler添加到ChannelPipeline
            pipeline.addLast(new HttpClientCodec());
            pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
        }
    }

}
