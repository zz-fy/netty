package cn.zfy.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @Classname TextWebSocketServerHandler
 * @Description
 * @Date 2022/5/31 13:42
 * @Created by zfy
 */
public class TextWebSocketServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String text = ((TextWebSocketFrame) msg).text();
        //将收到的额
        ctx.channel().writeAndFlush(new TextWebSocketFrame("客户端: " + text));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.fillInStackTrace();
        ctx.close();
    }
}
