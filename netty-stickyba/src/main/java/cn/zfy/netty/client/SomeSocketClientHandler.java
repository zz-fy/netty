package cn.zfy.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * @Classname SomeSocketClientHandler
 * @Description
 * @Date 2022/5/31 10:39
 * @Created by zfy
 */
public class SomeSocketClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for (int i = 0; i < 100; i++) {
            ctx.writeAndFlush("hello world" + i + " ");
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.fillInStackTrace();
        ctx.close();
    }

}
