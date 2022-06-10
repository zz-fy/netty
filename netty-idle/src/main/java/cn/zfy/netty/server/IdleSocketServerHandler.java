package cn.zfy.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;

/**
 * @Classname IdleSocketServerHandler
 * @Description 服务端处理器
 * @Date 2022/6/6 16:13
 * @Created by zfy
 */
public class IdleSocketServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


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

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object msg) throws Exception {
        //若由空闲事件触发
        if (msg instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) msg;
            //事件状态描述对象
            String eventDes = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventDes = "读空闲超时";
                    break;
                case WRITER_IDLE:
                    eventDes = "写空闲超时";
                    break;
                case ALL_IDLE:
                    eventDes = "读写空闲超时";
            }
            System.out.println(ctx.channel().remoteAddress() + ": " + eventDes);
            ctx.channel().close();
        } else {
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

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.fillInStackTrace();
        ctx.close();
    }
}
