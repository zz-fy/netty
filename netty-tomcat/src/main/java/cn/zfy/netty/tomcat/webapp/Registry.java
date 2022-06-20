package cn.zfy.netty.tomcat.webapp;

import cn.zfy.netty.tomcat.servlet.Servlet;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Classname Registry
 * @Description
 * @Date 2022/6/16 16:00
 * @Created by zfy
 */
public class Registry {

    // 加载并缓存指定包中的所有的servlet类名名称
    private List<String> servletNameCache = new ArrayList<>();


    //注册servlet
    public void registerServlet(String basePackage, Map<String, Object> registerMap) throws Exception {
        // 对指定包中的servlet类名进行缓存
        cacheServletClass(basePackage);
        if (servletNameCache.isEmpty()) return;
        for (String className : servletNameCache) {
            Class<?> clazz = Class.forName(className);
            Type[] interfaces = clazz.getGenericInterfaces();
            for (Type anInterface : interfaces) {
                if (anInterface != Servlet.class) continue;
                //获取到当前遍历servlet的全小写字母的简单类名
                String simpleClassName = className.substring(className.lastIndexOf(".") + 1).toLowerCase();
                //注册servlet到servlet注册表
                registerMap.put(simpleClassName, clazz.newInstance());
            }
        }

    }

    /**
     * 对指定包中的servlet类名进行缓存
     *
     * @param basePackage
     */
    private void cacheServletClass(String basePackage) {
        URL resource = this.getClass().getClassLoader().getResource(basePackage.replaceAll("\\.", "/"));
        if (Objects.isNull(resource)) return;
        File dir = new File(resource.getFile());
        for (File file : dir.listFiles()) {
            String fileName = file.getName();
            if (file.isDirectory()) cacheServletClass(basePackage.concat(".").concat(fileName));
            if (!fileName.endsWith(".class")) continue;
            servletNameCache.add(basePackage.concat(".").concat(fileName).replace(".class", ""));
        }
    }
}
