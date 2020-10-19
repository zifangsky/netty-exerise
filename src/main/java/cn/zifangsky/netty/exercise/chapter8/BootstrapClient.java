package cn.zifangsky.netty.exercise.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 引导客户端
 *
 * @author zifangsky
 * @date 2020/10/19
 * @since 1.0.0
 */
public class BootstrapClient {

    public static void main(String[] args) throws InterruptedException {
        BootstrapClient client = new BootstrapClient();
        client.bootstrap();
    }

    /**
     * Bootstrapping a client
     */
    public void bootstrap() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("127.0.0.1", 8080))
                    .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                            System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
                        }
                    });

            ChannelFuture future = bootstrap.connect().sync();
            future.addListener(channelFuture -> {
                if (channelFuture.isSuccess()) {
                    System.out.println("Connection established");
                } else {
                    System.err.println("Connection attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            });
        }finally {
            group.shutdownGracefully().sync();
        }
    }

}
