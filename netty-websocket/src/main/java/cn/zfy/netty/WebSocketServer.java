package cn.zfy.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Classname WebSocketServer
 * @Description 服务端启动类
 * @Date 2022/5/31 13:36
 * @Created by zfy
 */
public class WebSocketServer {


    public static void main(String[] args) {
        EventLoopGroup parentEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup childEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentEventLoopGroup, childEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new HttpServerCodec())
                                .addLast(new ChunkedWriteHandler())
                                .addLast(new HttpObjectAggregator(4096))
                                .addLast(new WebSocketServerProtocolHandler("/some"))
                                .addLast(new TextWebSocketServerHandler());
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind(8092).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            parentEventLoopGroup.shutdownGracefully();
            childEventLoopGroup.shutdownGracefully();
        }
    }


}
