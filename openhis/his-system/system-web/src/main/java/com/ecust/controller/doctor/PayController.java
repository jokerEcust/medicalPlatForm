package com.ecust.controller.doctor;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ecust.service.OrderChargeService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Reference
    OrderChargeService orderChargeService;


    @RequestMapping("/callback/{orderId}")
    public void callback(@PathVariable String orderId, HttpServletRequest request) {
        Map<String, String> parameterMap = this.getParameterMap(request);
        String trade_status = parameterMap.get("trade_status");
        boolean checkV1 = false;
        try {
            checkV1 = AlipaySignature.rsaCheckV1(parameterMap, env.getProperty("alipay_public_key"), "utf-8", env.getProperty("sign_type"));
            if (checkV1) {
                //说明是支付宝手段进入
                String refund_fee = parameterMap.get("refund_fee");
                if (refund_fee == null) {
                    //收费
                    String trade_no = parameterMap.get("trade_no");
                    System.out.println("收费成功平台ID" + trade_no);
                    orderChargeService.paySuccess(orderId, trade_no);
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
