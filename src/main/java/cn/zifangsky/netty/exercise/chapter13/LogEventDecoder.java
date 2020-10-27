package cn.zifangsky.netty.exercise.chapter13;

import cn.zifangsky.netty.exercise.utils.DateUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * 日志消息解码器
 *
 * @author zifangsky
 * @date 2020/10/27
 * @since 1.0.0
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
        //获取对DatagramPacket中的数据（ByteBuf）的引用
        ByteBuf data = datagramPacket.content();
        //获取SEPARATOR的索引位置
        int idx = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR);
        //获取文件路径
        String filePath = data.slice(0, idx).toString(CharsetUtil.UTF_8);
        //重新设置readerIndex
        data.readerIndex(idx + 1);
        //获取日志信息
        String logMsg = data.slice(idx + 1, data.readableBytes()).toString(CharsetUtil.UTF_8);

        //创建一个新的LogEvent
        LogEvent logEvent = new LogEvent(datagramPacket.sender(), DateUtils.nowStr(), filePath, logMsg);
        out.add(logEvent);
    }
}