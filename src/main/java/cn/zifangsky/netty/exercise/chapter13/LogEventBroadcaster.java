package cn.zifangsky.netty.exercise.chapter13;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * 引导服务端
 *
 * @author zifangsky
 * @date 2020/10/27
 * @since 1.0.0
 */
public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;

    public LogEventBroadcaster(InetSocketAddress address, File file) {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(this.group)
                //引导该NioDatagramChannel（无连接的）
                .channel(NioDatagramChannel.class)
                //设置SO_BROADCAST套接字选项
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(address));

        this.file = file;
    }

    public void run() throws Exception {
        //绑定Channel
        Channel ch = bootstrap.bind(0).sync().channel();

        long pointer = 0;
        for (;;) {
            long len = file.length();
            if (len < pointer) {
                // file was reset
                pointer = len;
            } else if (len > pointer) {
                // Content was added
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {
                    ch.writeAndFlush(
                            new LogEvent(null, null, file.getName(),
                                    new String(line.getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.UTF_8))
                    );
                }
                pointer = raf.getFilePointer();
                raf.close();
            }

            TimeUnit.SECONDS.sleep(5);
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException();
        }
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(
                new InetSocketAddress("255.255.255.255", Integer.parseInt(args[0])), new File(args[1]));

        try {
            broadcaster.run();
        }
        finally {
            broadcaster.stop();
        }
    }
}
