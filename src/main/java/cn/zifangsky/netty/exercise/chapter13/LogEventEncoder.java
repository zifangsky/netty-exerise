package cn.zifangsky.netty.exercise.chapter13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 日志消息编码器
 *
 * @author zifangsky
 * @date 2020/10/27
 * @since 1.0.0
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
    private final InetSocketAddress remoteAddress;

    public LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
        byte[] filePathBytes = logEvent.getFilePath().getBytes(CharsetUtil.UTF_8);
        byte[] msgBytes = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);

        ByteBuf buf = ctx.alloc().buffer(filePathBytes.length + msgBytes.length + 1);
        //写入数据
        buf.writeBytes(filePathBytes);
        buf.writeByte(LogEvent.SEPARATOR);
        buf.writeBytes(msgBytes);

        //创建一个DatagramPacket并添加到出站的消息列表中
        out.add(new DatagramPacket(buf, this.remoteAddress));
    }
}