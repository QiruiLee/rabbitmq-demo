package com.lqr.msgConfirm;

import com.lqr.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Description 消息确认
 * @Author lqr
 * @Date 2023/2/7 16:05
 * @Version 1.0
 **/
public class Test {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //单个确认
//        singleConfirm();
        //批量确认
//        batchConfirm();
        //异步批量确认
//        asyncConfirm();


//        testInt();
        testStr();
    }

    private static void testInt() {
        //Integer i1 = Integer.valueOf("1");
        //Integer i2 = Integer.valueOf("1");
        //i1==i2因为valueof方式走的是缓存，缓存-128~127。地址一样

//        Integer i1 = new Integer("1");
//        Integer i2 = new Integer("1");
        //i1!=i2因为new，地址不一样
//        System.out.println(i1==i2);
    }

    private static void testStr() {
//        String a = new String("1");
//        String b = new String("1");
//        a!=b因为new返回新对象

        String a = "1";
        String b = "1";
        //a==b因为字面量赋值时，jvm先找字符串常量池，有则返回引用
        System.out.println(a==b);
    }

    public static void singleConfirm() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        //开启发布确认
        channel.confirmSelect();

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            channel.basicPublish("", QUEUE_NAME, null, (i + "").getBytes());
            channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        System.out.println("共耗时" + (end - begin) + "ms");
    }

    public static void batchConfirm() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        //开启发布确认
        channel.confirmSelect();

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            channel.basicPublish("", QUEUE_NAME, null, (i + "").getBytes());
            if (i % 100 == 0) {
                channel.waitForConfirms();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("共耗时" + (end - begin) + "ms");
    }

    public static void asyncConfirm() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        //开启发布确认
        channel.confirmSelect();


        //存未确认的消息
        ConcurrentSkipListMap<Long, String> unConfirmedMap = new ConcurrentSkipListMap<>();
        //确认成功回调
        ConfirmCallback ackCallBack = (deliveryTag, multiple) -> {
            System.out.println("成功tag" + deliveryTag);
            if (multiple) {
                unConfirmedMap.headMap(deliveryTag);
            } else {
                unConfirmedMap.remove(deliveryTag);
            }
        };
        //确认失败回调
        ConfirmCallback nackCallBack = (deliveryTag, multiple) -> {
            System.out.println("失败tag" + deliveryTag);
        };
        //增加监听
        channel.addConfirmListener(ackCallBack, nackCallBack);

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String msg = (i + "");
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            //记录发的消息
            unConfirmedMap.put(channel.getNextPublishSeqNo(), msg);
        }

        long end = System.currentTimeMillis();
        System.out.println("共耗时" + (end - begin) + "ms");
    }
}
