package cn.zifangsky.netty.exercise.chapter8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 引导服务器
 *
 * @author zifangsky
 * @date 2020/10/19
 * @since 1.0.0
 */
public class BootstrapServer {

    public static void main(String[] args) throws InterruptedException {
        BootstrapServer server = new BootstrapServer();
        server.bootstrap();
    }

    /**
     * Bootstrapping a server
     */
    public void bootstrap() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(8080))
                    .childHandler(new SimpleChannelInboundHandler<ByteBuf>(){

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                            System.out.println("Server received: " + msg.toString(CharsetUtil.UTF_8));
                        }
                    });

            ChannelFuture future = bootstrap.bind().sync();
            future.addListener(channelFuture -> {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("Bind attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            });
        }finally {
            group.shutdownGracefully().sync();
        }
    }

}
