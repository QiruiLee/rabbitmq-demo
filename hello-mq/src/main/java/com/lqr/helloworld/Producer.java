package com.lqr.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * @Description 生产者发消息
 * @Author lqr
 * @Date 2023/2/7 11:14
 * @Version 1.0
 **/
public class Producer {
    //队列名称
    public static final String QUEUE_NAME="hello";
    public static final String HOST_IP="192.168.200.130";
    public static final int HOST_PORT=5672;
    public static final String USER_NAME="admin";
    public static final String PASSWORD="123";

    public static void main(String[] args) throws Exception{
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST_IP);
        factory.setPort(HOST_PORT);
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /**
         * 生成队列
         * 1、队列名称
         * 2、队列里面的消息是否持久化 true:到磁盘    默认false存在内存
         * 3、队列是否进行消息共享 true:多个消费者   false:只供一个消费者消费
         * 4、是否自动删除 最后一个消费者断开连接后，该队列是否自动删除
         * 5、其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发消息
        String message = "hello world";
        /**
         * 发送消息
         * 1、交换机
         * 2、路由的key值，这里写队列名称
         * 3、其他参数
         * 4、消息的Byte数组
         */
        channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_BASIC,message.getBytes());
        System.out.println("消息发送完成");
    }
}
