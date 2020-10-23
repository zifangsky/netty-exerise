package cn.zifangsky.netty.exercise.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Class ShortToByteEncoder
 *
 * @author zifangsky
 * @date 2020/10/22
 * @since 1.0.0
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
        //将Short写入ByteBuf 中
        out.writeShort(msg);
    }
}
