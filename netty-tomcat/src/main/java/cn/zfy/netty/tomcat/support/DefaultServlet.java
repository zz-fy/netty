package cn.zfy.netty.tomcat.support;

import cn.zfy.netty.tomcat.servlet.HttpServletRequest;
import cn.zfy.netty.tomcat.servlet.HttpServletResponse;
import cn.zfy.netty.tomcat.servlet.Servlet;

/**
 * @Classname DefaultServlet
 * @Description
 * @Date 2022/6/13 16:58
 * @Created by zfy
 */
public class DefaultServlet implements Servlet {
    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String servletName = httpServletRequest.getUri().split("/")[1];
        String content = "404 - no this servlet ".concat(servletName);
        httpServletResponse.write(content);
    }

    @Override
    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        doGet(httpServletRequest, httpServletResponse);
    }
}
