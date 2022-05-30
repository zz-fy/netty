package cn.zfy.netty.socket.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @Classname SomeSocketClientHandler
 * @Description 客户端处理器
 *
 * SimpleChannelInboundHandler中的channelRead()方法会自动释放接收到的来自于对方的msg
 * 所占有的所有资源
 *
 * @Date 2022/5/30 16:26
 * @Created by zfy
 */
public class SomeSocketClientHandler extends SimpleChannelInboundHandler<String> {



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "," + msg);
        ctx.channel().writeAndFlush("from client:".concat(String.valueOf(UUID.randomUUID())));
        TimeUnit.MILLISECONDS.sleep(500);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("from client: begin talking..");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.fillInStackTrace();
        ctx.close();
    }

}
