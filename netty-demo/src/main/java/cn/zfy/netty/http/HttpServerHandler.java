package cn.zfy.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @Classname HttpServerHandler
 * @Description 服务端处理器
 * @Date 2022/5/30 15:02
 * @Created by zfy
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof HttpRequest)) return;
        HttpRequest request = (HttpRequest) msg;
        String uri = request.uri();
        if ("/favicon.ico".equalsIgnoreCase(uri)) return;
        System.out.println(String.format("请求方式:%s", request.method().name()));
        System.out.println(String.format("请求URI:%s", uri));
        ByteBuf content = Unpooled.copiedBuffer("hello netty", CharsetUtil.UTF_8);
        DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        HttpHeaders headers = httpResponse.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.fillInStackTrace();
        ctx.close();
    }
}
