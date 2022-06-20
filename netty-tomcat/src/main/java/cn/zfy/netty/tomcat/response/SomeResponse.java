package cn.zfy.netty.tomcat.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Classname SomeResponse
 * @Description
 * @Date 2022/6/20 10:49
 * @Created by zfy
 */
@Data
@Builder
public class SomeResponse {
    private Map<String, List<String>> parameters;
    private String uri;
    private String method;
    private String path;
}
