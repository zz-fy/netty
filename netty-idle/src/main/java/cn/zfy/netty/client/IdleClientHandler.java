package cn.zfy.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Classname IdleClientHandler
 * @Description
 * @Date 2022/6/1 10:35
 * @Created by zfy
 */
public class IdleClientHandler extends SimpleChannelInboundHandler<String> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println(msg);
    }
}
