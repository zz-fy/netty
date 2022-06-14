package cn.zfy.netty.tomcat.support;

import cn.zfy.netty.tomcat.servlet.HttpServletRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Classname DefaultHttpServletRequest
 * @Description
 * @Date 2022/6/13 16:41
 * @Created by zfy
 */
public class DefaultHttpServletRequest implements HttpServletRequest {

    private HttpRequest httpRequest;

    public DefaultHttpServletRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    @Override
    public String getUri() {
        return httpRequest.uri();
    }

    @Override
    public String getMethod() {
        return httpRequest.method().name();
    }

    @Override
    public Map<String, List<String>> getParameters() {
        QueryStringDecoder decoder = new QueryStringDecoder(httpRequest.uri());
        return decoder.parameters();
    }

    @Override
    public List<String> getParameters(String name) {
        Map<String, List<String>> parameters = getParameters();
        if (Objects.isNull(parameters) || parameters.isEmpty()) return Collections.emptyList();
        return parameters.get(name);
    }

    @Override
    public String getParameter(String name) {
        List<String> parameters = getParameters(name);
        if (Objects.isNull(parameters) || parameters.isEmpty()) return null;
        return parameters.get(0);
    }

    @Override
    public String getPath() {
        QueryStringDecoder decoder = new QueryStringDecoder(httpRequest.uri());
        return decoder.path();
    }
}
