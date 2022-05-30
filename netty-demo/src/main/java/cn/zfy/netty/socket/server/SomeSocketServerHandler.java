package cn.zfy.netty.socket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Classname SomeSocketServerHandler
 * @Description 服务端处理器
 * <p>
 * ChannelInboundHandlerAdapter 中的 channelRead()方法不会自动释放接收到的来自于对方的
 * msg
 * @Date 2022/5/30 16:01
 * @Created by zfy
 */
public class SomeSocketServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "," + msg);
        ctx.channel().writeAndFlush("from server:".concat(String.valueOf(UUID.randomUUID())));
        TimeUnit.MILLISECONDS.sleep(500);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.fillInStackTrace();
        ctx.close();
    }

}
