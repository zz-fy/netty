package cn.zfy.netty.tomcat.webapp;

import cn.zfy.netty.tomcat.response.SomeResponse;
import cn.zfy.netty.tomcat.servlet.HttpServletRequest;
import cn.zfy.netty.tomcat.servlet.HttpServletResponse;
import cn.zfy.netty.tomcat.servlet.Servlet;
import cn.zfy.netty.tomcat.support.DefaultServlet;
import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Classname SomeServlet
 * @Description 当找不到客户端所访问的servlet时会调用该默认的servlet
 * @Date 2022/6/16 15:55
 * @Created by zfy
 */
public class SomeServlet implements Servlet {

    //http://localhost:8888/someservlet/xxx/xxx?name=zs
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, List<String>> parameters = request.getParameters();
        String uri = request.getUri();
        String method = request.getMethod();
        String path = request.getPath();
        response.write(JSON.toJSONString(SomeResponse.builder().parameters(parameters).uri(uri).method(method).path(path).build()));
    }

    @Override
    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        doGet(httpServletRequest, httpServletResponse);
    }

}
