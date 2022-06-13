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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Classname RpcServer
 * @Description 服务器类
 * @Date 2022/6/13 9:50
 * @Created by zfy
 */
public class RpcServer {

    // 定义服务注册表
    private Map<String, Object> registryMap = new ConcurrentHashMap<>();

    //缓存指定包中所有提供者的类名
    private List<String> classCache = Collections.synchronizedList(new ArrayList<>());

    /**
     * 将指定包中的所有提供者写入服务注册表
     *
     * @param providerPackage
     * @throws Exception
     */
    public void publish(String providerPackage) throws Exception {
        getProviderClass(providerPackage);
        doRegister();
        EventLoopGroup parentEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup childEventLoopGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentEventLoopGroup, childEventLoopGroup)
                //用于指定当服务端请求处理线程全部用完时，临时存放已经完成了三次握手的请求的队列的长度
                .option(ChannelOption.SO_BACKLOG, 1024)
                //指定是否启用心跳机制来维护C/S间的长连接
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                //指定要创建的Channel类型
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


    //将指定包中所有class文件的类名写入到classCache中
    private void getProviderClass(String providerPackage) {
        // cn.zfy.netty.rpc.server.service ---> cn/zfy/netty/rpc/server/service
        URL resource = this.getClass().getClassLoader().getResource(providerPackage.replaceAll("\\.", "/"));
        if (Objects.isNull(resource)) return;
        File dir = new File(resource.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                getProviderClass(getFullyQualifiedName(providerPackage, file.getName()));
                continue;
            }
            if (file.getName().endsWith(".class")) {
                //获取的是全限定名
                String fileName = file.getName().replace(".class", "").trim();
                classCache.add(getFullyQualifiedName(providerPackage, fileName));
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

    //获取类的全限定名
    private String getFullyQualifiedName(String basePackage, String fileName) {
        return basePackage.concat(".").concat(fileName);
    }

}
