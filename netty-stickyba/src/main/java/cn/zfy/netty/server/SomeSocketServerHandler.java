package cn.zfy.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Classname SomeSocketServerHandler
 * @Description
 * @Date 2022/5/31 10:54
 * @Created by zfy
 */
public class SomeSocketServerHandler extends SimpleChannelInboundHandler<String> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("server收到第[" + ++count + "]个数据包:" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.fillInStackTrace();
        ctx.close();
    }

}
