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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * @Classname WebSocketClient
 * @Description 客户端A
 * @Date 2022/6/1 10:32
 * @Created by zfy
 */
public class WebSocketClientA {

    public static void main(String[] args) {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new LineBasedFrameDecoder(2048))
                                .addLast(new StringEncoder(CharsetUtil.UTF_8))
                                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                                .addLast(new ChatSocketClientHandler());
                    }
                });
        try {
            ChannelFuture future = bootstrap.connect("localhost", 8093).sync();
            Channel channel = future.channel();
            InputStreamReader inputStreamReader = new InputStreamReader(System.in, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (true) {
                channel.writeAndFlush(bufferedReader.readLine() + "\r\n");
            }

        } catch (InterruptedException | UnsupportedEncodingException e) {
            if (!Objects.isNull(eventLoopGroup)) eventLoopGroup.shutdownGracefully();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
