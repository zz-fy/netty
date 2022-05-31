package cn.zfy.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;

/**
 * @Classname SomeSocketServer
 * @Description 服务端启动类
 * @Date 2022/5/31 10:51
 * @Created by zfy
 */
public class SomeSocketServer {

    public static void main(String[] args) {
        EventLoopGroup parentEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup childEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentEventLoopGroup, childEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8))
                                .addLast(new SomeSocketServerHandler());
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind(8090).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            parentEventLoopGroup.shutdownGracefully();
            childEventLoopGroup.shutdownGracefully();
        }

    }

}
