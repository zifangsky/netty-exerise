package cn.zifangsky.netty.exercise.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * FixedLengthFrameDecoder
 *
 * @author zifangsky
 * @date 2020/10/19
 * @since 1.0.0
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //检查是否有足够的字节可以被读取，以生成下一个帧
        while (in.readableBytes() >= this.frameLength){
            //从ByteBuf中读取一个新帧
            ByteBuf buf = in.readBytes(this.frameLength);
            //将该帧添加到已被解码的消息列表中
            out.add(buf);
        }
    }
}