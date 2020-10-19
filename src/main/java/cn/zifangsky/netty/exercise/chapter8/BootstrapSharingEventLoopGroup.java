package cn.zifangsky.netty.exercise.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * 引导服务器
 *
 * @author zifangsky
 * @date 2020/10/19
 * @since 1.0.0
 */
public class BootstrapSharingEventLoopGroup {

    public static void main(String[] args) throws InterruptedException {
        BootstrapSharingEventLoopGroup server = new BootstrapSharingEventLoopGroup();
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
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>(){
                    ChannelFuture connectFuture;

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        //创建一个Bootstrap类的实例以连接到远程主机
                        Bootstrap bootstrap = new Bootstrap();

                        //使用与分配给已被接受的子Channel相同的EventLoop
                        bootstrap.group(ctx.channel().eventLoop())
                                .channel(NioSocketChannel.class)
                                .remoteAddress(new InetSocketAddress("192.168.100.2", 8080))
                                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
                                    }
                                });

                        connectFuture = bootstrap.connect();
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        if (connectFuture.isDone()) {
                            //连接完成时，执行一些数据操作（如代理）
                        }
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
    }

}
