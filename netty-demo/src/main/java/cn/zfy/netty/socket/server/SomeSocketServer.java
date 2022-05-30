package cn.zfy.netty.socket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @Classname SomeSocketServer
 * @Description 服务端启动类
 * <p>
 * 本例要实现的功能是：客户端连接上服务端后，其马上会向服务端发送一个数据。服务
 * 端在接收到数据后，会马上向客户端也回复一个数据。客户端每收到服务端的一个数据后，
 * 便会再向服务端发送一个数据。而服务端每收到客户端的一个数据后，便会再向客户端发送
 * 一个数据。如此反复，无穷匮也
 * @Date 2022/5/30 15:55
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
                        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new SomeSocketServerHandler());
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind(8089).sync();
            System.out.println(String.format("端口:%d,服务器已启动..", 8089));
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            parentEventLoopGroup.shutdownGracefully();
            childEventLoopGroup.shutdownGracefully();
        }

    }

}
