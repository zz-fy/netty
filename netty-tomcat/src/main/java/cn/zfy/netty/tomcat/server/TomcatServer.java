package cn.zfy.netty.tomcat.server;

import cn.zfy.netty.tomcat.servlet.Servlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname TomcatServer
 * @Description 服务器启动类
 * @Date 2022/6/14 9:05
 * @Created by zfy
 */
public class TomcatServer {

    //用于缓存指定包中的所有的类的全限定类名（value)
    private Map<String, String> classNameMap = new ConcurrentHashMap<>();

    //用于缓存指定包中所有Servlet类的实例
    private Map<String, Servlet> instanceMap = new ConcurrentHashMap<>();

    public void start() {
        cacheClassName("cn.zfy.netty.tomcat.webapp");
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentGroup, childGroup)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpServerCodec())
                                .addLast(new TomcatServerHandler(classNameMap, instanceMap));
                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            System.out.println("tomcat 启动成功,端口号 8888 ");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    private void cacheClassName(String basePackage) {
        URL resource = this.getClass().getClassLoader().getResource(basePackage.replaceAll("\\.", "/"));
        File dir = new File(resource.getFile());
        for (File file : dir.listFiles()) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                cacheClassName(basePackage.concat(".").concat(fileName));
            }
            if (!fileName.endsWith(".class")) continue;
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            String servletName = basePackage.concat(".").concat(fileName);
            classNameMap.put(fileName.toLowerCase(), servletName);
        }


    }
}
