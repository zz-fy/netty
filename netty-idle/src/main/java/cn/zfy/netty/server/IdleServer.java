package cn.zfy.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @Classname IdleServer
 * @Description
 * @Date 2022/6/6 14:38
 * @Created by zfy
 */
public class IdleServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup parentEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup childEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentEventLoopGroup, childEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(3, 5, 0))
                                .addLast(new IdleSocketServerHandler());
                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(9000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            parentEventLoopGroup.shutdownGracefully();
            childEventLoopGroup.shutdownGracefully();
        }


    }

}
