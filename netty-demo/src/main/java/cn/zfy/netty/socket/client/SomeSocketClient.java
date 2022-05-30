package cn.zfy.netty.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Objects;

/**
 * @Classname SomeSocketClient
 * @Description 客户端启动类
 * @Date 2022/5/30 16:04
 * @Created by zfy
 */
public class SomeSocketClient {

    public static void main(String[] args) {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap clientBootStrap = new Bootstrap();
        clientBootStrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new SomeSocketClientHandler());
                    }
                });
        try {
            ChannelFuture future = clientBootStrap.connect("localhost", 8089).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            if (!Objects.isNull(eventLoopGroup)) eventLoopGroup.shutdownGracefully();
        }

    }

}
