package cn.zifangsky.netty.exercise.chapter5;

import io.netty.buffer.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * {@link ByteBuf}的基本使用
 *
 * @author zifangsky
 * @date 2020/10/13
 * @since 1.0.0
 */
public class ByteBufExamples {
    private final static Random RANDOM = new Random();
    private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = null;

    /**
     * Backing array
     */
    private void heapBuffer(){
        ByteBuf heapBuf = BYTE_BUF_FROM_SOMEWHERE;
        //检查 ByteBuf 是否有一个支撑数组
        if(heapBuf.hasArray()){
            //如果有，则获取对该数组的引用
            byte[] arr = heapBuf.array();
            //计算第一个字节的偏移量
            int offset = heapBuf.arrayOffset();
            //获得可读字节数
            int length = heapBuf.readableBytes();
            //使用数组、偏移量和长度作为参数调用你的方法
            this.handleArray(arr, offset, length);
        }
    }

    /**
     * Direct buffer data access
     */
    private void directBuffer(){
        ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE;
        //检查ByteBuf 是否由数组支撑。如果不是，则这是一个直接缓冲区
        if(!directBuf.hasArray()){
            //获得可读字节数
            int length = directBuf.readableBytes();
            //分配一个新的数组来保存具有该长度的字节数据
            byte[] arr = new byte[length];
            //将字节复制到该数组
            directBuf.getBytes(directBuf.readerIndex(), arr);
            //使用数组、偏移量和长度作为参数调用你的方法
            this.handleArray(arr, 0, length);
        }
    }

    /**
     * Composite buffer pattern using ByteBuffer
     */
    private void byteBufferComposite(ByteBuffer header, ByteBuffer body) {
        ByteBuffer[] message = new ByteBuffer[]{header, body};
        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());

        message2.put(header);
        message2.put(body);
        message2.flip();

        //其他操作
    }

    /**
     * Composite buffer pattern using CompositeByteBuf
     */
    private void byteBufComposite(ByteBuf header, ByteBuf body) {
        CompositeByteBuf message = Unpooled.compositeBuffer();
        //将ByteBuf实例追加到CompositeByteBuf
        message.addComponents(header, body);

        //删除位于索引位置为 0（第一个组件）的ByteBuf
        message.removeComponent(0);

        //循环遍历所有的ByteBuf实例
        for(ByteBuf buf : message){
            System.out.println(buf.toString(CharsetUtil.UTF_8));
        }

        //其他操作
    }

    /**
     * Accessing the data in a CompositeByteBuf
     */
    private void byteBufCompositeArray() {
        CompositeByteBuf compBuf = Unpooled.compositeBuffer();

        //获得可读字节数
        int length = compBuf.readableBytes();
        //分配一个新的数组来保存具有该长度的字节数据
        byte[] arr = new byte[length];
        //将字节复制到该数组
        compBuf.getBytes(compBuf.readerIndex(), arr);
        //使用数组、偏移量和长度作为参数调用你的方法
        this.handleArray(arr, 0, length);
    }

    /**
     * Access data
     */
    private void byteBufRelativeAccess(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;

        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        }
    }

    /**
     * Read all data
     */
    private void readAllData(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;

        while (buffer.isReadable()) {
            System.out.println(buffer.readByte());
        }
    }

    /**
     * Write data
     */
    private void write(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;

        while (buffer.writableBytes() >= 4) {
            buffer.writeInt(RANDOM.nextInt());
        }
    }

    /**
     * Using ByteProcessor to find \r
     */
    private void byteProcessor(){
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;

        int index = buffer.forEachByte(ByteProcessor.FIND_CR);
    }

    /**
     * Using Slice a ByteBuf
     */
    private void byteBufSlice(){
        ByteBuf buffer = Unpooled.copiedBuffer("Netty in Action rocks!", CharsetUtil.UTF_8);
        //创建一个从索引0开始到索引5结束的一个新切片
        ByteBuf sliced = buffer.slice(0, 5);
        System.out.println(sliced.toString(CharsetUtil.UTF_8));

        //更新索引0处的字节
        buffer.setByte(0, 'A');
        //此断言将会成功，因为数据是共享的，对其中一个所做的更改对另外一个也是可见的
        assert buffer.getByte(0) == sliced.getByte(0);
//        System.out.println(sliced.toString(CharsetUtil.UTF_8));
    }

    /**
     * get() and set() usage
     */
    private void byteBufSetGet(){
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", CharsetUtil.UTF_8);
        //打印第一个字符'N'
        System.out.println((char)buf.getByte(0));

        //获取当前的readerIndex 和writerIndex
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();

        //更新索引0处的字节
        buf.setByte(0, 'A');
        //再次打印第一个字符，此时是'A'
        System.out.println((char)buf.getByte(0));

        //此断言将会成功，因为这些操作并不会修改相应的索引
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }

    /**
     * read() and write() usage
     */
    private void byteBufWriteRead(){
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", CharsetUtil.UTF_8);
        //打印第一个字符'N'
        System.out.println((char)buf.readByte());

        //获取当前的readerIndex 和writerIndex
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();

        //将字符'?'追加到缓冲区
        buf.writeByte('?');
        //重新打印最后一个字符，此时是'?'
        System.out.println((char)buf.getByte(buf.writerIndex() - 1));

        //此断言将会成功，因为writeByte()方法移动了writerIndex
        assert readerIndex == buf.readerIndex();
        assert writerIndex != buf.writerIndex();
    }

    /**
     * {@link ByteBufHolder}
     */
    private void byteBuff(){
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", CharsetUtil.UTF_8);
        ByteBufHolder holder = new DefaultByteBufHolder(buf);
        //创建一个深拷贝
        ByteBuf copied = holder.copy().content();
    }

    /**
     * Obtaining a ByteBufAllocator reference
     */
    private void obtainingByteBufAllocatorReference(){
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        //从Channel获取一个到ByteBufAllocator的引用
        ByteBufAllocator allocator = channel.alloc();

        //get reference form somewhere
        ChannelHandlerContext ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;
        //从ChannelHandlerContext 获取一个到ByteBufAllocator 的引用
        ByteBufAllocator allocator2 = ctx.alloc();
        //...
    }






    @Test
    public void test(){
        byteBufWriteRead();
    }


    private void handleArray(byte[] array, int offset, int len) {

    }

}
