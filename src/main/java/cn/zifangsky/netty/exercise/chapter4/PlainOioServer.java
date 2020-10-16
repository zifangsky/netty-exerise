package cn.zifangsky.netty.exercise.chapter4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * JDK API中的阻塞IO实现的网络编程
 *
 * @author zifangsky
 * @date 2020/10/12
 * @since 1.0.0
 */
@Slf4j
public class PlainOioServer {


    public void server(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);

        while (true){
            final Socket clientSocket = socket.accept();
            System.out.println("Accepted connection from " + clientSocket);

            //创建一个新的线程来处理该连接
            Thread thread = new Thread(() -> {
                OutputStream out;

                try {
                    out = clientSocket.getOutputStream();
                    //将消息写给已连接的客户端
                    out.write("Hi!\r\n".getBytes(StandardCharsets.UTF_8));
                    out.flush();

                    //关闭连接
                    clientSocket.close();
                }catch (IOException e){
                    log.error("An exception has occurred here!", e);
                }finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        log.error("An exception has occurred here!", e);
                    }
                }
            });

            //启动线程
            thread.start();
        }
    }

}
