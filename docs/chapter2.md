
因为你的Echo服务器会响应传入的消息，所以它需要实现ChannelInboundHandler接口，用来定义响应入站事件的方法。这个简单的应用程序只需要用到少量的这些方法，所以继承`ChannelInboundHandlerAdapter`类也就足够了，它提供了`ChannelInboundHandler`的默认实现。

- `channelRead()`——对于每个传入的消息都要调用；
- `channelReadComplete()`——通知ChannelInboundHandler最后一次对channel-Read()的调用是当前批量读取中的最后一条消息；
- `exceptionCaught()`——在读取操作期间，有异常抛出时会调用。

#### 服务端ChannelHandler ####

```java
package cn.zifangsky.netty.exercise.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 负责处理的{@link ChannelHandler}实例
 *
 * @author zifangsky
 * @date 2020/9/18
 * @since 1.0.0
 */
@Slf4j
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对接收消息的处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;

        //1. 打印接收到的消息
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        //2. 将接收到的消息重新发送给发送者
        ctx.write(in);
        ctx.flush();
    }

    /**
     * Read Complete的处理
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 对异常的处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //1. 打印日志
        log.error("数据处理过程中发生异常！", cause);
        //2. 关闭Channel
        ctx.close();
    }
}
```



#### 服务端代码 ####

```java
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
```



#### 客户端ChannelHandler ####

```java
package cn.zifangsky.netty.exercise.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * ChannelHandler for the client
 *
 * @author zifangsky
 * @date 2020/9/18
 * @since 1.0.0
 */
@Slf4j
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知Channel是活跃的时候，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //打印已接收的消息
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //1. 打印日志
        log.error("数据处理过程中发生异常！", cause);
        //2. 关闭Channel
        ctx.close();
    }
}
```



#### 客户端代码 ####

```java
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

```

