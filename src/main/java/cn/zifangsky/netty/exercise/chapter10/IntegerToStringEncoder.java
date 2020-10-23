package cn.zifangsky.netty.exercise.chapter10;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Class IntegerToStringEncoder
 *
 * @author zifangsky
 * @date 2020/10/22
 * @since 1.0.0
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Short> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Short msg, List<Object> out) throws Exception {
        //将Integer转换为String，并将其添加到List中
        out.add(String.valueOf(msg));
    }
}
