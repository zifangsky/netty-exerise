package cn.zifangsky.netty.exercise.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * 服务端处理类
 *
 * @author zifangsky
 * @date 2020/9/18
 * @since 1.0.0
 */
@Slf4j
public class EchoServer {
    /**
     * 监听端口
     */
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();

        //1. 创建Event-LoopGroup
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //2. 创建Server-Bootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    //指定使用NIO传输的Channel
                    .channel(NioServerSocketChannel.class)
                    //指定监听端口
                    .localAddress(new InetSocketAddress(port))
                    //添加一个EchoServer-Handler到子Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(serverHandler);
                        }
                    });

            //3. 异步绑定服务器，并调用sync()方法阻塞等待直到绑定完成
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println(EchoServer.class.getName() +
                    " started and listening for connections on " + future.channel().localAddress());
            //4. 获取Channel的CloseFuture，并阻塞当前线程直到它完成
            future.channel().closeFuture().sync();
        }finally {
            //5. 关闭EventLoopGroup，释放所有的资源
            group.shutdownGracefully().sync();
        }
    }


    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }
}
