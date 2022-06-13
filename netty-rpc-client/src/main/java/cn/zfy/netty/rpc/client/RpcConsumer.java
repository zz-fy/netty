package cn.zfy.netty.rpc.client;

import cn.zfy.netty.rpc.api.service.SomeService;

/**
 * @Classname RpcConsumer
 * @Description 消费类
 * @Date 2022/6/13 10:31
 * @Created by zfy
 */
public class RpcConsumer {

    public static void main(String[] args) {
        SomeService someService = RpcProxy.create(SomeService.class);
        someService.doSome("zfy..");
    }

}
