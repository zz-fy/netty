package cn.zfy.netty.tomcat.servlet;

/**
 * @Classname HttpServletResponse
 * @Description 响应接口
 * @Date 2022/6/13 16:36
 * @Created by zfy
 */
public interface HttpServletResponse {

    void write(String content) throws Exception;

}
