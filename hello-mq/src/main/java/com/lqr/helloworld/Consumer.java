package com.lqr.helloworld;

import com.rabbitmq.client.*;

/**
 * @Description 消费者
 * @Author lqr
 * @Date 2023/2/7 11:58
 * @Version 1.0
 **/
public class Consumer {
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

        //成功消费的回调
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("成功消费:"+new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //取消消费的回调
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("取消消费:"+consumerTag);
        };
        /**
         * 消费信息
         * 1、消费哪个队列
         * 2、消费成功后是否自动应答 true自动，false手动
         * 3、成功消费的回调
         * 4、取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
