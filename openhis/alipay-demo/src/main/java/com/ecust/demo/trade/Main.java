package com.ecust.demo.trade;

import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    private static Properties env = new Properties();

    static {
        InputStream inputStream = null;
        try {
            inputStream = Main.class.getResourceAsStream("/zfbsetting.properties");
            env.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //收费
        Map<String, Object> pay = PayService.pay(
                env.getProperty("outTradeNo"),
                env.getProperty("subject"),
                env.getProperty("totalAmount"),
                env.getProperty("sellerId"),
                env.getProperty("body"),
                env.getProperty("operatorId"),
                env.getProperty("storeId"),
                env.getProperty("timeoutExpress"),
                env.getProperty("notifyUrl") + env.getProperty("outTradeNo"));
        System.out.println(pay);
    }

    @Test
    public void testRefund() {
        //测试退费
        String outTradeNo=env.getProperty("outTradeNo");
        String refundAmount=env.getProperty("refundAmount");
        String refundReason=env.getProperty("refundReason");
        String outRequestNo=env.getProperty("outRequestNo");
        String storeId=env.getProperty("storeId");
        PayService.payRefund(outTradeNo,refundAmount,refundReason,outRequestNo,storeId);
    }

    @Test
    public void q(){
//        Long a=1L;
//        Long b=2L;
//        System.out.println(a-b);
        Map<Integer,Integer> map=new HashMap<>();
        map.put(1,1);
        map.put(2,2);
        System.out.println(map.toString());
    }

}
