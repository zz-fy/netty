package cn.zfy.netty.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Objects;

/**
 * @Classname SocketClient
 * @Description 客户端启动类
 * 客户端作为发送方，向服务端发送两个大的 ByteBuf 数据包，这两个数据包会被拆分为
 * 若干个 Frame 进行发送。这个过程中会发生拆包与粘包。
 * 服务端作为接收方，直接将接收到的 Frame 解码为 String 后进行显示，不对这些 Frame
 * 进行粘包与拆包
 * @Date 2022/5/31 10:29
 * @Created by zfy
 */
public class SomeSocketClient {

    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new SomeSocketClientHandler());
                    }
                });
        try {
            ChannelFuture future = bootstrap.connect("localhost", 8090).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (!Objects.isNull(eventLoopGroup)) {
                eventLoopGroup.shutdownGracefully();
            }
        }

    }

}
