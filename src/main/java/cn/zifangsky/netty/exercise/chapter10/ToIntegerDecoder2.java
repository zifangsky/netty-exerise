package cn.zifangsky.netty.exercise.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * Class ToIntegerDecoder2 extends ReplayingDecoder
 *
 * @author zifangsky
 * @date 2020/10/22
 * @since 1.0.0
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //传入的ByteBuf是ReplayingDecoderByteBuf类型
        out.add(in.readInt());
    }
}

