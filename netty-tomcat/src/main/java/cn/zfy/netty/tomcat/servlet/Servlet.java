package cn.zfy.netty.tomcat.servlet;

/**
 * @Classname Servlet
 * @Description
 * @Date 2022/6/13 16:35
 * @Created by zfy
 */
public interface Servlet {

    void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception;

    void doPost(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws Exception;


}
