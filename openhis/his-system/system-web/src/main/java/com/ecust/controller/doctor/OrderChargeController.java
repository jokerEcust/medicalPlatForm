package com.ecust.controller.doctor;

import cn.hutool.core.bean.BeanUtil;
import com.ecust.alipay.PayService;
import com.ecust.constants.Constants;
import com.ecust.controller.BaseController;
import com.ecust.domain.*;
import com.ecust.dto.OrderChargeDto;
import com.ecust.dto.OrderChargeFormDto;
import com.ecust.dto.OrderChargeItemDto;
import com.ecust.service.CareService;
import com.ecust.service.OrderChargeService;
import com.ecust.utils.IdGeneratorSnowflake;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/doctor/charge")
public class OrderChargeController extends BaseController {
    private static Properties env = new Properties();

    static {
        try (InputStream inputStream = OrderChargeController.class.getResourceAsStream("/zfbsetting.properties");) {
            env.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Reference
    private CareService careService;


    @Reference
    OrderChargeService orderChargeService;

    /**
     * 根据挂号id查询未支付的处方信息和详情
     *
     * @param regId
     * @return
     */
    @GetMapping("/getNoChargeCareHistoryByRegId/{regId}")
    public AjaxResult getNoChargeCareHistoryByRegId(@PathVariable String regId) {
        CareHistory careHistory = careService.queryCareHistoryByRegId(regId);
        if (careHistory == null) {
            return AjaxResult.fail("挂号单号为【" + regId + "】的挂号单不存在，请核对后查询");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("careHistory", careHistory);
        res.put("careOrders", Collections.EMPTY_LIST);
        List<Map<String, Object>> list = new ArrayList<>();
        List<CareOrder> careOrders = careService.queryCareOrdersByChId(careHistory.getChId());
        if (careOrders == null || careOrders.size() == 0) {
            return AjaxResult.fail("挂号单号为【" + regId + "】的处方信息不存在，请核对后查询");
        }
        for (CareOrder careOrder : careOrders) {
            Map<String, Object> beanToMap = BeanUtil.beanToMap(careOrder);
            beanToMap.put("careOrderItems", Collections.EMPTY_LIST);
            BigDecimal allAmount = new BigDecimal("0");
            List<CareOrderItem> careOrderItems = careService.queryCareOrderItemsByCoId(careOrder.getCoId(), Constants.ORDER_DETAILS_STATUS_0);
            if (careOrderItems == null || careOrderItems.size() == 0) {
                continue;
            } else {
                for (CareOrderItem careOrderItem : careOrderItems) {
                    allAmount = allAmount.add(careOrderItem.getAmount());
                }
                beanToMap.put("careOrderItems", careOrderItems);
                beanToMap.put("allAmount", allAmount);
                list.add(beanToMap);
            }
        }
        if (list.isEmpty()) {
            return AjaxResult.fail("【" + regId + "】的挂号单不存在未支付的处方信息，请核对后再查询");
        } else {
            res.put("careOrders", list);
            return AjaxResult.success(res);
        }

    }

    /**
     * 现金支付
     *
     * @param orderChargeFormDto
     * @return
     */
    @PostMapping("/createOrderChargeWithCash")
    public AjaxResult createOrderChargeWithCash(@RequestBody @Valid OrderChargeFormDto orderChargeFormDto) {
        //保存订单以及订单详情
        //先定义未支付订单存储进数据库，然后再修改为支付状态
        orderChargeFormDto.getOrderChargeDto().setPayType(Constants.PAY_TYPE_0);//现金支付
        orderChargeFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        orderChargeFormDto.getOrderChargeDto().setOrderId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_ODC));
        orderChargeService.saveOrderAndOrderItems(orderChargeFormDto);
        orderChargeService.paySuccess(orderChargeFormDto.getOrderChargeDto().getOrderId(), null);
        return AjaxResult.success("创建订单并现金支付成功");

    }

    /**
     * 创建支付宝收费订单
     *
     * @return
     */
    @PostMapping("/createOrderChargeWithZfb")
    public AjaxResult createOrderChargeWithZfb(@RequestBody @Valid OrderChargeFormDto orderChargeFormDto) {
        orderChargeFormDto.getOrderChargeDto().setPayType(Constants.PAY_TYPE_1);//支付宝支付
        orderChargeFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        orderChargeFormDto.getOrderChargeDto().setOrderId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_ODC));
        orderChargeService.saveOrderAndOrderItems(orderChargeFormDto);
        String outTradeNo = orderChargeFormDto.getOrderChargeDto().getOrderId();
        String subject = env.getProperty("subject");
        String totalAmount = orderChargeFormDto.getOrderChargeDto().getOrderAmount().toString();
        String sellerId = "";
        String body = "";
        List<OrderChargeItemDto> orderChargeItemDtoList = orderChargeFormDto.getOrderChargeItemDtoList();
        for (OrderChargeItemDto orderChargeItemDto : orderChargeItemDtoList) {
            body += orderChargeItemDto.getItemName() + "-" + orderChargeItemDto.getItemPrice() + " ";
        }
        String operatorId = env.getProperty("operatorId");
        String storeId = env.getProperty("storeId");
        String timeoutExpress = env.getProperty("timeoutExpress");
        String notifyUrl = env.getProperty("notifyUrl") + outTradeNo;

        Map<String, Object> pay = PayService.pay(outTradeNo, subject, totalAmount, sellerId, body, operatorId, storeId, timeoutExpress, notifyUrl);
        String qrCode = pay.get("qrCode").toString();
        if (qrCode != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", outTradeNo);
            map.put("allAmount", totalAmount);
            map.put("payUrl", qrCode);
            return AjaxResult.success(map);
        }
        return AjaxResult.fail(pay.get("msg").toString());
    }

    /**
     * 根据订单ID查询订单信息【验证是否支付成功】
     *
     * @param orderId
     * @return
     */
    @GetMapping("/queryOrderChargeOrderId/{orderId}")
    public AjaxResult queryOrderChargeOrderId(@PathVariable String orderId) {
        OrderCharge orderCharge = orderChargeService.queryOrderChargeByOrderId(orderId);
        if (orderCharge == null) {
            return AjaxResult.fail("【" + orderId + "】订单号所在的订单不存在，请核对后再输入");
        }
        if (!orderCharge.getPayType().equals(Constants.PAY_TYPE_1)) {
            return AjaxResult.fail("【" + orderId + "】订单号所在的订单不属于电子支付范畴，请核对后再输入");
        }
        return AjaxResult.success(orderCharge);
    }

    /**
     * 分页查询所有收费单
     *
     * @return
     */
    @GetMapping("/queryAllOrderChargeForPage")
    public AjaxResult queryAllOrderChargeForPage(OrderChargeDto orderChargeDto) {
        DataGridView dataGridView = orderChargeService.queryAllOrderChargeForPage(orderChargeDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    @GetMapping("/queryOrderChargeItemByOrderId/{orderId}")
    public AjaxResult queryOrderChargeItemByOrderId(@PathVariable String orderId) {
        List<OrderChargeItem> orderChargeItems = orderChargeService.queryOrderChargeItemByOrderId(orderId);
        return AjaxResult.success(orderChargeItems);
    }

    @GetMapping("/payWithCash/{orderId}")
    public AjaxResult payWithCash(@PathVariable String orderId) {
        OrderCharge orderCharge = this.orderChargeService.queryOrderChargeByOrderId(orderId);
        if (orderCharge == null) {
            return AjaxResult.fail("【" + orderId + "】订单号所在的订单不存在，请核对后再输入");
        }
        if (orderCharge.getOrderStatus().equals(Constants.ORDER_STATUS_1)) {
            return AjaxResult.fail("【" + orderId + "】订单号不是未支付状态，请核对后再输入");
        }
        orderChargeService.paySuccess(orderId, null);
        return AjaxResult.success();
    }

    @GetMapping("toPayOrderWithZfb/{orderId}")
    public AjaxResult toPayOrderWithZfb(@PathVariable String orderId) {
        OrderCharge orderCharge = this.orderChargeService.queryOrderChargeByOrderId(orderId);
        if (null == orderCharge) {
            return AjaxResult.fail("【" + orderId + "】订单号所在的订单不存在，请核对后再输入");
        }
        if (orderCharge.getOrderStatus().equals(Constants.ORDER_STATUS_1)) {
            return AjaxResult.fail("【" + orderId + "】订单号不是未支付状态，请核对后再输入");
        }
        String outTradeNo = orderId;
        String subject = env.getProperty("subject");
        String totalAmount = orderCharge.getOrderAmount().toString();
        String body = "";
        String sellerId = "";
        String operatorId = env.getProperty("operatorId");
        String storeId = env.getProperty("storeId");
        String timeoutExpress = env.getProperty("timeoutExpress");
        String notifyUrl = env.getProperty("notifyUrl") + outTradeNo;
        Map<String, Object> pay = PayService.pay(outTradeNo, subject, totalAmount, sellerId, body, operatorId, storeId, timeoutExpress, notifyUrl);

        String qrCode = pay.get("qrCode").toString();
        if (qrCode != null) {
            //创建支付成功
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("allAmount", totalAmount);
            map.put("payUrl", qrCode);
            return AjaxResult.success(map);
        } else {
            return AjaxResult.fail(pay.get("msg").toString());
        }
    }
}
