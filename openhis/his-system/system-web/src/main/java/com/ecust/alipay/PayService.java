package com.ecust.alipay;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付服务
 * 封装核心方法
 */
@Log4j2
public class PayService {
    private static AlipayTradeService tradeService;

    static {
        Configs.init("zfbinfo.properties");
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    /**
     * 支付方法
     *
     * @param outTradeNo     订单号
     * @param subject        订单标题
     * @param totalAmount    总价格
     * @param sellerId       卖家支付宝ID
     * @param body           订单描述
     * @param operatorId     商品操作员编号
     * @param storeId        商品门店编号
     * @param timeoutExpress 支付超时时间
     * @param notifyUrl      支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
     * @return 键值对，msg和code
     */
    public static Map<String, Object> pay(String outTradeNo, String subject,
                                          String totalAmount, String sellerId,
                                          String body, String operatorId, String storeId,
                                          String timeoutExpress, String notifyUrl
    ) {
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject)
                .setTotalAmount(totalAmount)
                .setOutTradeNo(outTradeNo)
                .setSellerId(sellerId)
                .setBody(body)
                .setOperatorId(operatorId)
                .setStoreId(storeId)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(notifyUrl);//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        Map<String, Object> map = new HashMap<>();
        String msg = "";
        Integer code;
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");
                AlipayTradePrecreateResponse res = result.getResponse();
                String qrCode = res.getQrCode();
                map.put("qrCode", qrCode);
                msg = "下单成功";
                code = 200;
//                ZxingUtils.getQRCodeImge(res.getQrCode(), 256, filePath);
                break;

            case FAILED:
                log.error("支付宝预下单失败!!!");
                msg = "下单失败";
                code = 500;
                break;

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                msg = "系统异常";
                code = 404;
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                msg = "不支持的交易状态";
                code = 404;
                break;
        }
        map.put("msg", msg);
        map.put("code", code);
        return map;
    }

    /**
     * 退款
     *
     * @param outTradeNo   订单号
     * @param refundAmount 退款总金额
     * @param refundReason 退款原因
     * @param outRequestNo 退款请求号，多次的退款号不同
     * @param storeId      商家id
     */
    public static Map<String, Object> payRefund(String outTradeNo, String refundAmount, String refundReason,
                                                String outRequestNo, String storeId) {
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
                .setOutTradeNo(outTradeNo)
                .setRefundAmount(refundAmount)
                .setRefundReason(refundReason)
                .setOutRequestNo(outRequestNo)
                .setStoreId(storeId);
        AlipayF2FRefundResult result = tradeService.tradeRefund(builder);
        Map<String, Object> map = new HashMap<>();
        String msg = "";
        Integer code;
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝退款成功: )");
                code=200;
                msg="支付宝退款成功";
                map.put("tradeNo",result.getResponse().getTradeNo());
                break;

            case FAILED:
                log.error("支付宝退款失败!!!");
                code=500;
                msg=result.getResponse().getSubMsg();
                break;

            case UNKNOWN:
                log.error("系统异常，订单退款状态未知!!!");
                code=500;
                msg=result.getResponse().getSubMsg();
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                code=500;
                msg=result.getResponse().getSubMsg();
                break;
        }
        map.put("code",code);
        map.put("msg",msg);
        return map;
    }
}

