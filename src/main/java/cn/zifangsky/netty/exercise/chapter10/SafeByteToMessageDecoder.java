package cn.zifangsky.netty.exercise.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * TooLongFrameException
 *
 * @author zifangsky
 * @date 2020/10/22
 * @since 1.0.0
 */
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {
    private static final int MAX_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readable = in.readableBytes();
        //检查缓冲区中是否有超过MAX_FRAME_SIZE个字节
        if (readable > MAX_FRAME_SIZE) {
            //跳过所有的可读字节，抛出TooLongFrame-Exception 并通知ChannelHandler
            in.skipBytes(readable);
            throw new TooLongFrameException("Frame too big!");
        }

        //do something else
    }
}
