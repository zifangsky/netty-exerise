package cn.zifangsky.netty.exercise.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * Bootstrapping and Using attributes
 *
 * @author zifangsky
 * @date 2020/10/19
 * @since 1.0.0
 */
public class BootstrapClientWithOptionsAndAttrs {

    public static void main(String[] args) throws InterruptedException {
        BootstrapClientWithOptionsAndAttrs server = new BootstrapClientWithOptionsAndAttrs();
        server.bootstrap();
    }

    /**
     * Using attributes
     */
    public void bootstrap() throws InterruptedException {
        //创建一个AttributeKey以标识该属性
        final AttributeKey<Integer> id = AttributeKey.newInstance("ID");

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress("127.0.0.1", 8080))
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {

                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        //使用AttributeKey检索属性以及它的值
                        Integer idValue = ctx.channel().attr(id).get();
                        // do something with the idValue
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
                    }
                });

        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        //设置该id属性
        bootstrap.attr(id, 123456);

        bootstrap.bind().sync();
    }
}
