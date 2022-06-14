package cn.zfy.netty.tomcat.support;

import cn.zfy.netty.tomcat.servlet.HttpServletResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.internal.StringUtil;

/**
 * @Classname DefaultHttpServletResponse
 * @Description
 * @Date 2022/6/13 16:51
 * @Created by zfy
 */
public class DefaultHttpServletResponse implements HttpServletResponse {

    private HttpRequest httpRequest;

    private ChannelHandlerContext channelHandlerContext;

    public DefaultHttpServletResponse(HttpRequest httpRequest, ChannelHandlerContext channelHandlerContext) {
        this.httpRequest = httpRequest;
        this.channelHandlerContext = channelHandlerContext;
    }


    @Override
    public void write(String content) throws Exception {
        if (StringUtil.isNullOrEmpty(content)) return;
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(content.getBytes("UTF-8"))
        );
        HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE, "text/json")
                .set(HttpHeaderNames.EXPIRES, 0)
                .set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if (HttpUtil.isKeepAlive(httpRequest)) {
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        channelHandlerContext.writeAndFlush(response);
    }
}
