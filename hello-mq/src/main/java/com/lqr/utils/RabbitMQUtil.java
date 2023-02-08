package com.lqr.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Description TODO
 * @Author lqr
 * @Date 2023/2/7 12:26
 * @Version 1.0
 **/
public class RabbitMQUtil {
    public static final String QUEUE_NAME = "hello";
    public static final String HOST_IP = "192.168.200.130";
    public static final int HOST_PORT = 5672;
    public static final String USER_NAME = "admin";
    public static final String PASSWORD = "123";

    public static Channel getChannel() {
        Channel channel = null;
        try {
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(HOST_IP);
            factory.setPort(HOST_PORT);
            factory.setUsername(USER_NAME);
            factory.setPassword(PASSWORD);
            //创建连接
            Connection connection = factory.newConnection();
            //获取信道
            channel = connection.createChannel();
        } catch (Exception e) {
        }
        return channel;
    }
}
