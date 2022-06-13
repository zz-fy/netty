package cn.zfy.netty.server;

/**
 * @Classname RpcServerStarter
 * @Description 服务端启动类
 * @Date 2022/6/13 10:09
 * @Created by zfy
 */
public class RpcServerStarter {

    public static void main(String[] args) throws Exception {
        new RpcServer().publish("cn.zfy.netty.rpc.api.service");
    }
}
