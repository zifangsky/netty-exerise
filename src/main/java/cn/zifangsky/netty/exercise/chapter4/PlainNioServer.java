package cn.zifangsky.netty.exercise.chapter4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * JDK API中的非阻塞IO实现的网络编程
 *
 * @author zifangsky
 * @date 2020/10/12
 * @since 1.0.0
 */
@Slf4j
public class PlainNioServer {

    public void server(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        ServerSocket serverSocket = serverSocketChannel.socket();
        //将服务器绑定到指定端口
        serverSocket.bind(new InetSocketAddress(port));

        //打开Selector来处理Channel
        Selector selector = Selector.open();
        //将ServerSocket注册到Selector，以接受连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes(StandardCharsets.UTF_8));

        while (true){
            try {
                //等待需要处理的新事件，阻塞将一直持续到下一个传入事件
                selector.select();
            }catch (IOException e){
                log.error("An exception has occurred here!", e);
                break;
            }

            //获取所有接收事件的 SelectionKey 实例
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();

            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();

                try {
                    //检查事件是否是一个新的已经就绪可以被接受的连接
                    if(key.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        //接受客户端，并将它注册到选择器
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                        System.out.println("Accepted connection from " + client);
                    }

                    //检查套接字是否已经准备好写数据
                    if(key.isWritable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while (buffer.hasRemaining()){
                            //将数据写到已连接的客户端
                            if(client.write(buffer) == 0){
                                break;
                            }
                        }

                        //关闭连接
                        client.close();
                    }
                }catch (IOException ex){
                    log.error("An exception has occurred here!", ex);
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        // ignore on close
                    }
                }
            }
        }
    }

}
