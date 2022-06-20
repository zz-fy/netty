package cn.zfy.netty.tomcat.server;

import cn.zfy.netty.tomcat.servlet.Servlet;
import cn.zfy.netty.tomcat.support.DefaultHttpServletRequest;
import cn.zfy.netty.tomcat.support.DefaultHttpServletResponse;
import cn.zfy.netty.tomcat.support.DefaultServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Map;

/**
 * @Classname TomcatServerHandler
 * @Description 服务端处理器
 * @Date 2022/6/14 9:14
 * @Created by zfy
 */
public class TomcatServerHandler extends ChannelInboundHandlerAdapter {


    //用于缓存指定包中的所有的类的全限定类名（value)
    private Map<String, String> classNameMap;

    //用于缓存指定包中所有Servlet类的实例
    private Map<String, Servlet> instanceMap;

    public TomcatServerHandler(Map<String, String> classNameMap, Map<String, Servlet> instanceMap) {
        this.classNameMap = classNameMap;
        this.instanceMap = instanceMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof HttpRequest)) return;
        HttpRequest request = (HttpRequest) msg;
        //从请求中获取到要访问的servlet名称
        String servletName = request.uri().split("/")[1];
        Servlet servlet;
        if (instanceMap.containsKey(servletName)) {
            servlet = instanceMap.get(servletName);
        } else if (classNameMap.containsKey(servletName)) {
            String className = classNameMap.get(servletName);
            servlet = (Servlet) Class.forName(className).newInstance();
            instanceMap.put(servletName, servlet);
        } else {
            servlet = new DefaultServlet();
        }
        DefaultHttpServletRequest req = new DefaultHttpServletRequest(request);
        DefaultHttpServletResponse rep = new DefaultHttpServletResponse(request, ctx);
        String methodName = request.method().name();
        if ("GET".equalsIgnoreCase(methodName)) {
            servlet.doGet(req, rep);
        } else if ("POST".equalsIgnoreCase(methodName)) {
            servlet.doPost(req, rep);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.fillInStackTrace();
    }
}
