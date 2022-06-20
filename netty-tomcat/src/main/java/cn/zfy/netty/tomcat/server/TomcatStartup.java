package cn.zfy.netty.tomcat.server;

/**
 * @Classname TomcatStartup
 * @Description
 * @Date 2022/6/20 10:24
 * @Created by zfy
 */
public class TomcatStartup {

    public static void main(String[] args) throws Exception {
        TomcatServer tomcatServer = new TomcatServer();
        tomcatServer.start();
    }

}
