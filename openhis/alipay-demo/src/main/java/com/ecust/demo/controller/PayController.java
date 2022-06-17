package com.ecust.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/pay")
public class PayController {
    private static Properties env = new Properties();

    static {
        InputStream inputStream = null;
        try {
            inputStream = PayController.class.getResourceAsStream("/zfbinfo.properties");
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

    @GetMapping("/hello")
    public String hello() {
        return "hello79";
    }

    @RequestMapping("/callback/{orderId}")
    public void callback(@PathVariable String orderId, HttpServletRequest request) {
        Map<String, String> parameterMap = this.getParameterMap(request);
        String trade_status = parameterMap.get("trade_status");
        //验证是否为支付宝发来的信息
//        System.out.println("trade_status:"+trade_status);
//        System.out.println(parameterMap);
//        System.out.println(JSON.toJSON(parameterMap));
        boolean checkV1 = false;
        try {
            checkV1 = AlipaySignature.rsaCheckV1(parameterMap, env.getProperty("alipay_public_key"), "utf-8", env.getProperty("sign_type"));
//            System.out.println("验证签名结果："+checkV1);
            if (checkV1) {
                //说明是支付宝手段进入
                String refund_fee = parameterMap.get("refund_fee");
                if (refund_fee == null) {
                    //收费
                    System.out.println("收费成功平台ID" + parameterMap.get("trade_no"));
                } else {
                    //退费
                    System.out.println("退费成功，退费的订单id为：" + parameterMap.get("out_biz_no"));
                }
                System.out.println(JSON.toJSON(parameterMap));

            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            System.out.println(orderId + "验证签名出现异常");
        }


    }

    private Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }
}
