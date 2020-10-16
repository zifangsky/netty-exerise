package io.netty.channel;

/**
 * 自定义{@link DefaultChannelPipeline}
 *
 * @author zifangsky
 * @date 2020/10/15
 * @since 1.0.0
 */
public class DummyChannelPipeline extends DefaultChannelPipeline {
    public static final ChannelPipeline DUMMY_INSTANCE = new DummyChannelPipeline(null);

    public DummyChannelPipeline(Channel channel) {
        super(channel);
    }
}
