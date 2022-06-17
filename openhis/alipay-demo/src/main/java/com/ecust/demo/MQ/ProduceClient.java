package com.ecust.demo.MQ;

public class ProduceClient {
    public static void main(String[] args) throws Exception{
        MqClient mqClient=new MqClient();
//        mqClient.produce("hello world");
        String consume = mqClient.consume();
        System.out.println(consume);
    }
}
