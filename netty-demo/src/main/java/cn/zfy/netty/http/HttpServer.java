package cn.zfy.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Classname HttpServer
 * @Description 服务启动
 * @Date 2022/5/30 14:57
 * @Created by zfy
 */
public class HttpServer {


    public static void main(String[] args) {
        EventLoopGroup parentEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup childEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentEventLoopGroup, childEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpChannelInitializer());
        try {
            ChannelFuture future = serverBootstrap.bind(8088).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            parentEventLoopGroup.shutdownGracefully();
            childEventLoopGroup.shutdownGracefully();
        }

    }
}
