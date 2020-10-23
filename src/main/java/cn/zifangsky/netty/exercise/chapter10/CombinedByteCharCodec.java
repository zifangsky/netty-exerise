package cn.zifangsky.netty.exercise.chapter10;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * CombinedChannelDuplexHandler<I,O>
 *
 * @author zifangsky
 * @date 2020/10/22
 * @since 1.0.0
 */

public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {

    public CombinedByteCharCodec() {
        //将委托实例传递给父类
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}