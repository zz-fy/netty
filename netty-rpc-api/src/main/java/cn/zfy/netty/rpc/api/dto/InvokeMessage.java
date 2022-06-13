package cn.zfy.netty.rpc.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname InvokeMessage
 * @Description 客户端(消费者)发送给服务端的服务调用信息
 * @Date 2022/6/13 9:39
 * @Created by zfy
 */
@Data
public class InvokeMessage implements Serializable {

    // 服务信息
    private String className;

    //要调用的方法名称
    private String methodName;

    //方法参数列表
    private Class<?>[] paramTypes;

    //方法参数值
    private Object[] paramValues;

}
