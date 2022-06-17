package com.ecust.controller.doctor;

import cn.hutool.core.bean.BeanUtil;
import com.ecust.alipay.PayService;
import com.ecust.constants.Constants;
import com.ecust.controller.BaseController;
import com.ecust.domain.*;
import com.ecust.dto.OrderBackfeeDto;
import com.ecust.dto.OrderBackfeeFormDto;
import com.ecust.dto.OrderBackfeeItemDto;
import com.ecust.dto.OrderChargeItemDto;
import com.ecust.service.CareService;
import com.ecust.service.OrderBackfeeService;
import com.ecust.service.OrderChargeService;
import com.ecust.utils.IdGeneratorSnowflake;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("doctor/backfee")
public class OrderBackfeeController extends BaseController {
    private static Properties env = new Properties();

    static {
        try (InputStream inputStream = OrderChargeController.class.getResourceAsStream("/zfbsetting.properties");) {
            env.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Reference
    CareService careService;

    @Reference
    OrderChargeService orderChargeService;

    @Reference
    OrderBackfeeService orderBackfeeService;

    @GetMapping("getChargedCareHistoryByRegId/{regId}")
    @HystrixCommand
    public AjaxResult getChargedCareHistoryByRegId(@PathVariable String regId){
        CareHistory careHistory = careService.queryCareHistoryByRegId(regId);
        if (careHistory==null){
            return AjaxResult.fail("【"+regId+"】的挂号单没有对应的病例信息，请核对后再查询");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("careHistory",careHistory);
        map.put("careOrders", Collections.EMPTY_LIST);
        List<Map<String,Object>> careOrdersList=new ArrayList<>();
        List<CareOrder> careOrders = careService.queryCareOrdersByChId(careHistory.getChId());
        if (careOrders.isEmpty()){
            return AjaxResult.fail("【"+regId+"】的挂号单没相关的处方信息，请核对后再查询");
        }
        for (CareOrder careOrder : careOrders) {
            Map<String, Object> map1 = BeanUtil.beanToMap(careOrder);
            map1.put("careOrderItems",Collections.EMPTY_LIST);
            BigDecimal allAmount=new BigDecimal("0");
            List<CareOrderItem> careOrderItems = careService.queryCareOrderItemsByCoId(careOrder.getCoId(), Constants.ORDER_DETAILS_STATUS_1);
            if (careOrderItems.isEmpty()){
                continue;
            }
            for (CareOrderItem careOrderItem : careOrderItems) {
                allAmount=allAmount.add(careOrderItem.getAmount());
            }
            map1.put("careOrderItems",careOrderItems);
            map1.put("allAmount",allAmount);
            careOrdersList.add(map1);
        }
        if (careOrdersList.isEmpty()){
            return AjaxResult.fail("【"+regId+"】的挂号单没已支付的处方信息，请核对后再查询");
        }else {
            map.put("careOrders",careOrdersList);
            return AjaxResult.success(map);
        }
    }

    @PostMapping("/createOrderBackfeeWithCash")
    public AjaxResult createOrderBackfeeWithCash(@Valid @RequestBody OrderBackfeeFormDto orderBackfeeFormDto){
//        OrderBackfeeDto orderBackfeeDto=orderBackfeeFormDto.getOrderBackfeeDto();
//        orderBackfeeFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
//        List<OrderBackfeeItemDto> orderBackfeeItemDtoList=orderBackfeeFormDto.getOrderBackfeeItemDtoList();
//        orderBackfeeDto.setBackType(Constants.PAY_TYPE_0);
//        String backId= IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_ODB);
//        orderBackfeeDto.setBackId(backId);
//        String itemId = orderBackfeeItemDtoList.get(0).getItemId();
//        OrderChargeItem orderChargeItem=this.orderChargeService.queryOrderChargeItemByItemId(itemId);
//        String orderId = orderChargeItem.getOrderId();
//        orderBackfeeDto.setOrderId(orderId);
//        orderBackfeeFormDto.setOrderBackfeeDto(orderBackfeeDto);
//        this.orderBackfeeService.saveOrderAndItems(orderBackfeeFormDto);
//        //因为是现金退费，所以直接更新详情状态
//        this.orderBackfeeService.backSuccess(backId,null,Constants.PAY_TYPE_0);
//        return AjaxResult.success("创建退费订单成功");
        //保存订单
        orderBackfeeFormDto.getOrderBackfeeDto().setBackType(Constants.PAY_TYPE_0);
        orderBackfeeFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
//生成退费单号
        String backId= IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_ODB);
        orderBackfeeFormDto.getOrderBackfeeDto().setBackId(backId);
//找到当前退费单之前的收费单的 ID
        String itemId = orderBackfeeFormDto.getOrderBackfeeItemDtoList().get(0).getItemId();
        OrderChargeItem
                orderChargeItem=this.orderChargeService.queryOrderChargeItemByItemId(itemId);
        orderBackfeeFormDto.getOrderBackfeeDto().setOrderId(orderChargeItem.getOrderId());
        this.orderBackfeeService.saveOrderAndItems(orderBackfeeFormDto);
//因为是现金退费，所以直接更新详情状态
        this.orderBackfeeService.backSuccess(backId,null,Constants.PAY_TYPE_0);
        return AjaxResult.success("创建现在退费订单成功");
    }


    @GetMapping("/queryAllOrderBackfeeForPage")
    public AjaxResult queryAllOrderBackfeeForPage(OrderBackfeeDto orderBackfeeDto){
        DataGridView dataGridView=orderBackfeeService.queryOrderBackfeeForPage(orderBackfeeDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    @GetMapping("/queryOrderBackfeeItemByBackId/{backId}")
    public AjaxResult queryOrderBackfeeItemByBackId(@PathVariable String backId){
        return AjaxResult.success(orderBackfeeService.queryOrderBackfeeItemByBackId(backId));
    }

    @PostMapping("/createOrderBackfeeWithZfb")
    public AjaxResult createOrderBackfeeWithZfb(@Valid @RequestBody OrderBackfeeFormDto orderBackfeeFormDto){
        //退费：在退费相关表增加数据，对订单表进行状态修改
        orderBackfeeFormDto.getOrderBackfeeDto().setBackType(Constants.PAY_TYPE_1);//支付宝退费
        orderBackfeeFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        String backId= IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_ODB);
        orderBackfeeFormDto.getOrderBackfeeDto().setBackId(backId);

        //找到当前退费单之前的收费单的 ID
        String itemId = orderBackfeeFormDto.getOrderBackfeeItemDtoList().get(0).getItemId();
        OrderChargeItem orderChargeItem=this.orderChargeService.queryOrderChargeItemByItemId(itemId);

        //根据订单号找到之前的支付定单对象 判断之前是否使用支付宝支付
        OrderCharge
                orderCharge=this.orderChargeService.queryOrderChargeByOrderId(orderChargeItem.getOrderId());
        if(orderCharge==null){
            return AjaxResult.fail("【"+orderBackfeeFormDto.getOrderBackfeeDto().getRegId()+"】的挂号单之前没有收费记录，不能使用支付宝退费，请核对后再查询");
        }
        if(!orderCharge.getPayType().equals(Constants.PAY_TYPE_1)){
            return AjaxResult.fail("【"+orderBackfeeFormDto.getOrderBackfeeDto().getRegId()+"】的挂号单之前的支付方式为现金，不能使用支付宝退费，请核对后再查询");
        }
        orderBackfeeFormDto.getOrderBackfeeDto().setOrderId(orderChargeItem.getOrderId());
        orderBackfeeService.saveOrderAndItems(orderBackfeeFormDto);

        String outTradeNo = orderBackfeeFormDto.getOrderBackfeeDto().getOrderId();
        String refundAmount = orderBackfeeFormDto.getOrderBackfeeDto().getBackAmount().toString();
        String refundReason="不需要：";
        List<OrderBackfeeItemDto> orderChargeBackfeeDtoList = orderBackfeeFormDto.getOrderBackfeeItemDtoList();
        for (OrderBackfeeItemDto orderBackfeeItemDto : orderChargeBackfeeDtoList) {
            refundReason += orderBackfeeItemDto.getItemName() + "-" + orderBackfeeItemDto.getItemPrice() + " ";
        }
        String storeId = env.getProperty("storeId");
        Map<String, Object> payBack = PayService.payRefund(outTradeNo, refundAmount, refundReason, backId, storeId);
        String code = payBack.get("code").toString();
        if (code.equals("200")) {
            orderBackfeeService.backSuccess(backId,payBack.get("tradeNo").toString(),Constants.PAY_TYPE_1);
            return AjaxResult.success();
        }
        return AjaxResult.fail(payBack.get("msg").toString());
    }
    

}
