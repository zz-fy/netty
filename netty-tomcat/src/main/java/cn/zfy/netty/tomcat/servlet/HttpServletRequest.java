package cn.zfy.netty.tomcat.servlet;

import java.util.List;
import java.util.Map;

/**
 * @Classname HttpServletRequest
 * @Description 请求接口
 * @Date 2022/6/13 16:35
 * @Created by zfy
 */
public interface HttpServletRequest {

    String getUri();

    String getMethod();

    Map<String, List<String>> getParameters();

    List<String> getParameters(String name);

    String getParameter(String name);

    String getPath();

}
