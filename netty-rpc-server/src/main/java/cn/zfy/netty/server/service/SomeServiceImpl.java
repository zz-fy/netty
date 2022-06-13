package cn.zfy.netty.server.service;

import cn.zfy.netty.rpc.api.service.SomeService;

/**
 * @Classname SomeServiceImpl
 * @Description api工程业务接口实现
 * @Date 2022/6/13 9:48
 * @Created by zfy
 */
public class SomeServiceImpl implements SomeService {
    @Override
    public void doSome(String s) {
        System.out.println("hello ".concat(s));
    }
}
