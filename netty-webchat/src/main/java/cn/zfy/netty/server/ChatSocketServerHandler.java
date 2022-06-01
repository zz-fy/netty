package cn.zfy.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;

/**
 * @Classname ChatSocketServerHandler
 * @Description
 * @Date 2022/5/31 14:28
 * @Created by zfy
 */
public class ChatSocketServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "发言:" + msg + "\n");
        group.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + ":" + msg + "\n");
            } else {
                ch.writeAndFlush("me: " + msg + "\n");
            }
        });
    }

    /**
     * channel 激活
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress address = channel.remoteAddress();
        System.out.println(address + "---上线");
        group.writeAndFlush(address + "--上线");
        group.add(channel);
    }

    /**
     * channel钝化
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress address = channel.remoteAddress();
        System.out.println(address + "--下线");
        group.writeAndFlush(address + "下线,在线人数: " + group.size() + "\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.fillInStackTrace();
        ctx.close();
    }
}
