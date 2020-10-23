package cn.zifangsky.netty.exercise.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Class ToIntegerDecoder extends ByteToMessageDecoder
 *
 * @author zifangsky
 * @date 2020/10/22
 * @since 1.0.0
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 4) {
            out.add(in.readInt());
        }
    }
}

