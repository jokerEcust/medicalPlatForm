package com.ecust.demo.MQ;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * broker负责接收、存储、转发消息
 */
public class Broker {
    private final static int MAX_SIZE=3;

    private static ArrayBlockingQueue<String> messageQueue=new ArrayBlockingQueue<>(MAX_SIZE);

    //生产消息
    public static void produce(String message){
        if (messageQueue.offer(message)){
            System.out.println("成功向消息中心投递消息:"+message+"，当前消息数量："+messageQueue.size());

        }
        else {
            System.out.println("消息处理中心达到最大容量");
        }
    }

    //消费消息
    public static String consume(){
        String message = messageQueue.poll();
        if (message!=null){
            System.out.println("成功消费消息:"+message+"，当前消息数量："+messageQueue.size());

        }
        else {
            System.out.println("消息处理中心没有可消费的消息");
        }
        return message;
    }


}
