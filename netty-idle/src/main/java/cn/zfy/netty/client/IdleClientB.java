package cn.zfy.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * @Classname IdleClient
 * @Description
 * @Date 2022/6/6 14:39
 * @Created by zfy
 */
public class IdleClientB {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline
                                .addLast(new StringEncoder(CharsetUtil.UTF_8))
                                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                                .addLast(new IdleClientHandler());
                    }
                });
        try {
            ChannelFuture future = bootstrap.connect("localhost", 9000).sync();
            Channel channel = future.channel();
            InputStreamReader inputStreamReader = new InputStreamReader(System.in, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                channel.writeAndFlush(bufferedReader.readLine() + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!Objects.isNull(eventLoopGroup)) eventLoopGroup.shutdownGracefully();
        }


    }

}
