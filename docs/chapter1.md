
## Netty的核心组件 ##

Netty的主要构件块：

- Channel；
- 回调；
- Future；
- 事件和ChannelHandler。

#### Channel ####

Channel是Java NIO的一个基本构造。目前，可以把Channel看作是传入（入站）或者传出（出站）数据的载体。因此，它可以被打开或者被关闭，连接或者断开连接。



#### 回调 ####

一个回调其实就是一个方法，一个指向已经被提供给另外一个方法的方法的引用。这使得后者可以在适当的时候调用前者。回调在广泛的编程场景中都有应用，而且也是在操作完成后通知相关方最常见的方式之一。

Netty在内部使用了回调来处理事件；当一个回调被触发时，相关的事件可以被一个interface-ChannelHandler的实现处理。

```java
public class ConnectHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx)
        throws Exception {    ← --  当一个新的连接已经被建立时，channelActive(ChannelHandlerContext)将会被调用
        System.out.println(
            "Client " + ctx.channel().remoteAddress() + " connected");
    }
}
```



#### Future ####

Future提供了另一种在操作完成时通知应用程序的方式。这个对象可以看作是一个异步操作的结果的占位符；它将在未来的某个时刻完成，并提供对其结果的访问。

JDK预置了interface java.util.concurrent.Future，但是其所提供的实现，只允许手动检查对应的操作是否已经完成，或者一直阻塞直到它完成。这是非常繁琐的，所以Netty提供了它自己的实现——ChannelFuture，用于在执行异步操作的时候使用。

**ChannelFuture提供了几种额外的方法，这些方法使得我们能够注册一个或者多个ChannelFutureListener实例**。监听器的回调方法operationComplete()，将会在对应的操作完成时被调用。然后监听器可以判断该操作是成功地完成了还是出错了。如果是后者，我们可以检索产生的Throwable。**简而言之，由ChannelFutureListener提供的通知机制消除了手动检查对应的操作是否完成的必要**。

每个Netty的出站I/O操作都将返回一个ChannelFuture；也就是说，它们都不会阻塞。正如我们前面所提到过的一样，Netty完全是异步和事件驱动的。

```java
Channel channel = ...;
// Does not block
ChannelFuture future = channel.connect(  ← -- 异步地连接到远程节点
    new InetSocketAddress("192.168.0.1", 25));
future.addListener(new ChannelFutureListener() {   ← --  注册一个ChannelFutureListener，以便在操作完成时获得通知
    @Override
    public void operationComplete(ChannelFuture future) { ← --  ❶ 检查操作
的状态
       if (future.isSuccess()){ 
            ByteBuf buffer = Unpooled.copiedBuffer(  ← -- 如果操作是成功的，则创建一个ByteBuf以持有数据
               "Hello",Charset.defaultCharset());
           ChannelFuture wf = future.channel()
                .writeAndFlush(buffer);   ← -- 将数据异步地发送到远程节点。
返回一个ChannelFuture
            ....
        } else {
            Throwable cause = future.cause();　　← --　如果发生错误，则访问描述原因的Throwable
            cause.printStackTrace();
        }
    }
});
```

需要注意的是，对错误的处理完全取决于你、目标，当然也包括目前任何对于特定类型的错误加以的限制。例如，如果连接失败，你可以尝试重新连接或者建立一个到另一个远程节点的连接。

如果你把ChannelFutureListener看作是回调的一个更加精细的版本，那么你是对的。事实上，回调和Future是相互补充的机制；它们相互结合，构成了Netty本身的关键构件块之一。



#### 事件和ChannelHandler ####

Netty使用不同的事件来通知我们状态的改变或者是操作的状态。这使得我们能够基于已经发生的事件来触发适当的动作。这些动作可能是：

- 记录日志；
- 数据转换；
- 流控制；
- 应用程序逻辑。

Netty是一个网络编程框架，所以事件是按照它们与入站或出站数据流的相关性进行分类的。可能由入站数据或者相关的状态更改而触发的事件包括：

- 连接已被激活或者连接失活；
- 数据读取；
- 用户事件；
- 错误事件。

出站事件是未来将会触发的某个动作的操作结果，这些动作包括：

- 打开或者关闭到远程节点的连接；
- 将数据写到或者冲刷到套接字。

Netty的ChannelHandler为处理器提供了基本的抽象。我们会在适当的时候对ChannelHandler进行更多的说明，但是目前你可以认为每个Channel-Handler的实例都类似于一种为了响应特定事件而被执行的回调。

Netty提供了大量预定义的可以开箱即用的ChannelHandler实现，包括用于各种协议（如HTTP和SSL/TLS）的ChannelHandler。在内部，ChannelHandler自己也使用了事件和Future，使得它们也成为了你的应用程序将使用的相同抽象的消费者。