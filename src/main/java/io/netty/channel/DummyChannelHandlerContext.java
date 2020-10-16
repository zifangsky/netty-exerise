package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

/**
 * 自定义{@link AbstractChannelHandlerContext}
 *
 * @author zifangsky
 * @date 2020/10/15
 * @since 1.0.0
 */
public class DummyChannelHandlerContext extends AbstractChannelHandlerContext {
    public static ChannelHandlerContext DUMMY_INSTANCE = new DummyChannelHandlerContext(null, null, null);
    public DummyChannelHandlerContext(DefaultChannelPipeline pipeline, EventExecutor executor, String name) {
        super(pipeline, executor, name, null);
    }

    @Override
    public ChannelHandler handler() {
        return null;
    }
}
