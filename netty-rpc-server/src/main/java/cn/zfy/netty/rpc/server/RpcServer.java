package cn.zfy.netty.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname RpcServer
 * @Description 服务器类
 * @Date 2022/6/13 9:50
 * @Created by zfy
 */
public class RpcServer {

    private Map<String, Object> registryMap = new HashMap<>();

    private List<String> classCache = new ArrayList<>();

    public void publish(String providerPackage) throws Exception {
        getProviderClass(providerPackage);
        doRegister();
        EventLoopGroup parentEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup childEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentEventLoopGroup, childEventLoopGroup)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ObjectEncoder())
                                .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                .addLast(new RpcServerHandler(registryMap));
                    }
                });
        try {
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();
            System.out.println("微服务已注册成功，端口号8888");
            channelFuture.channel().closeFuture().sync();
        } finally {
            parentEventLoopGroup.shutdownGracefully();
            childEventLoopGroup.shutdownGracefully();
        }

    }


    private void getProviderClass(String providerPackage) {
        URL resource = this.getClass().getClassLoader().getResource(providerPackage.replaceAll("\\.", "/"));
        File dir = new File(resource.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                getProviderClass(providerPackage.concat(".").concat(file.getName()));
                continue;
            }
            if (file.getName().endsWith(".class")) {
                String fileName = file.getName().replace(".class", "").trim();
                classCache.add(providerPackage.concat(".").concat(fileName));
            }
        }
    }

    private void doRegister() throws Exception {
        if (classCache.size() == 0) return;
        for (String className : classCache) {
            Class<?> clazz = Class.forName(className);
            String interfaceName = clazz.getInterfaces()[0].getName();
            registryMap.put(interfaceName, clazz.newInstance());
        }
    }

}
