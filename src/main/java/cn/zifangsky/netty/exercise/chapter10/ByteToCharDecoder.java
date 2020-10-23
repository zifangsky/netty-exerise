package cn.zifangsky.netty.exercise.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Class ByteToCharDecoder
 *
 * @author zifangsky
 * @date 2020/10/22
 * @since 1.0.0
 */
public class ByteToCharDecoder extends ByteToMessageDecoder {

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 2) {
            out.add(in.readChar());
        }
    }
}