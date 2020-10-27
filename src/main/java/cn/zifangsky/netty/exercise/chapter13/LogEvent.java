package cn.zifangsky.netty.exercise.chapter13;

import lombok.Getter;

import java.net.InetSocketAddress;

/**
 * 日志消息
 *
 * @author zifangsky
 * @date 2020/10/27
 * @since 1.0.0
 */
@Getter
public final class LogEvent {
    public static final byte SEPARATOR = (byte) ':';
    /**
     * 消息来源
     */
    private final InetSocketAddress sourceAddress;
    /**
     * 日志文件名
     */
    private final String filePath;
    /**
     * 消息内容
     */
    private final String msg;
    /**
     * 接收消息的时间
     */
    private final String receivedTime;

    public LogEvent(String filePath, String msg) {
        this(null, null, filePath, msg);
    }

    public LogEvent(InetSocketAddress sourceAddress, String receivedTime,
                    String filePath, String msg) {
        this.sourceAddress = sourceAddress;
        this.receivedTime = receivedTime;
        this.filePath = filePath;
        this.msg = msg;
    }
}