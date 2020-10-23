package cn.zifangsky.netty.exercise.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Class CharToByteEncoder
 *
 * @author zifangsky
 * @date 2020/10/22
 * @since 1.0.0
 */
public class CharToByteEncoder extends MessageToByteEncoder<Character> {

    @Override
    public void encode(ChannelHandlerContext ctx, Character msg, ByteBuf out) throws Exception {
        out.writeChar(msg);
    }
}