package cn.zifangsky.netty.exercise.echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 客户端处理类
 *
 * @author zifangsky
 * @date 2020/9/18
 * @since 1.0.0
 */
public class EchoClient {
    private final String host;

    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        final EchoClientHandler clientHandler = new EchoClientHandler();

        //1. 创建Event-LoopGroup
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //2. 创建Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    //在创建Channel时，向ChannelPipeline中添加一个Echo-ClientHandler实例
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(clientHandler);
                        }
                    });
            //3. 连接到远程节点，阻塞等待直到连接完成
            ChannelFuture future = bootstrap.connect().sync();
            //4. 阻塞，直到Channel关闭
            future.channel().closeFuture().sync();
        }finally {
            //5. 关闭EventLoopGroup，释放所有的资源
            group.shutdownGracefully().sync();
        }
    }


    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>");
            return;
        }

        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();
    }
}
