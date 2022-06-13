package cn.zfy.netty.server;

import cn.zfy.netty.rpc.api.dto.InvokeMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

/**
 * @Classname RpcServerHandler
 * @Description 服务端处理器
 * @Date 2022/6/13 10:04
 * @Created by zfy
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> registryMap;

    public RpcServerHandler(Map<String, Object> registryMap) {
        this.registryMap = registryMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof InvokeMessage) {
            InvokeMessage message = (InvokeMessage) msg;
            Object result = "提供者没有提供方法";
            if (registryMap.containsKey(message.getClassName())) {
                Object provider = registryMap.get(message.getClassName());
                result = provider.getClass()
                        .getMethod(message.getMethodName(), message.getParamTypes())
                        .invoke(provider, message.getParamValues());
            }
            ctx.writeAndFlush(result);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.fillInStackTrace();
        ctx.close();
    }
}
