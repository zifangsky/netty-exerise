package cn.zifangsky.netty.exercise.chapter13;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 对解析后的日志信息的处理
 *
 * @author zifangsky
 * @date 2020/10/27
 * @since 1.0.0
 */
@Slf4j
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent event) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(event.getReceivedTime());
        builder.append(" [");
        builder.append(event.getSourceAddress().toString());
        builder.append("] [");
        builder.append(event.getFilePath());
        builder.append("] : ");
        builder.append(event.getMsg());
        System.out.println(builder.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印日志
        log.error("数据处理过程中发生异常！", cause);
        //关闭连接
        ctx.close();
    }
}
