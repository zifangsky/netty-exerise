package cn.zifangsky.netty.exercise.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * Bootstrapping and Using DatagramChannel
 *
 * @author zifangsky
 * @date 2020/10/19
 * @since 1.0.0
 */
public class BootstrapDatagramChannel {

    public static void main(String[] args) throws InterruptedException {
        BootstrapDatagramChannel server = new BootstrapDatagramChannel();
        server.bootstrap();
    }

    /**
     * Using DatagramChannel
     */
    public void bootstrap() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                //指定Channel的实现
                .channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                        System.out.println("Client received: " + msg.toString());
                    }
                });

        //调用bind()方法，因为该协议是无连接的
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(0));
        future.addListener(channelFuture -> {
            if (channelFuture.isSuccess()) {
                System.out.println("Connection established");
            } else {
                System.err.println("Connection attempt failed");
                channelFuture.cause().printStackTrace();
            }
        });
    }
}
