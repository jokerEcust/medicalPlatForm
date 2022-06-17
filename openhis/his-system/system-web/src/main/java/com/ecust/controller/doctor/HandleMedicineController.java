package com.ecust.controller.doctor;

import cn.hutool.core.bean.BeanUtil;
import com.ecust.constants.Constants;
import com.ecust.controller.BaseController;
import com.ecust.domain.CareHistory;
import com.ecust.domain.CareOrder;
import com.ecust.domain.CareOrderItem;
import com.ecust.service.CareService;
import com.ecust.vo.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/doctor/handleMedicine")
public class HandleMedicineController extends BaseController {
    @Reference
    private CareService careService;
    /**
     * 根据挂号单号查询支付的处方详情
     * @param regId
     * @return
     */
    @GetMapping("/getChargedCareHistoryOnlyMedicinesByRegId/{regId}")
    public AjaxResult getChargedCareHistoryOnlyMedicinesByRegId(@PathVariable String regId){
        CareHistory careHistory = careService.queryCareHistoryByRegId(regId);
        if (careHistory==null){
            return AjaxResult.fail("挂号单号【"+regId+"】不存在，请核对后查询");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("careHistory",careHistory);
        map.put("careOrders", Collections.EMPTY_LIST);
        //一个病例对应多个处方，一个处方对应多个订单详情
        List<Map<String,Object>> careOrderList=new ArrayList<>();
        List<CareOrder> careOrders = careService.queryCareOrdersByChId(careHistory.getChId());
        if (careOrders.isEmpty()){
            return AjaxResult.fail("挂号单号【"+regId+"】不存在订单信息，请核对后查询");
        }
        for (CareOrder careOrder : careOrders) {
            //如果是检查处方，则不处理
            if(careOrder.getCoType().equals(Constants.CO_TYPE_CHECK)){
                continue;
            }
            //药用处方
            Map<String, Object> careOrderMap = BeanUtil.beanToMap(careOrder);
            careOrderMap.put("careOrderItems",Collections.EMPTY_LIST);
            BigDecimal allAmount=new BigDecimal("0");
            List<CareOrderItem> careOrderItems = careService.queryCareOrderItemsByCoId(careOrder.getCoId(),Constants.ORDER_DETAILS_STATUS_1);
            if (careOrderItems.isEmpty()){
                continue;
            }else {
                for (CareOrderItem careOrderItem : careOrderItems) {
                    allAmount=allAmount.add(careOrderItem.getAmount());
                }
                careOrderMap.put("careOrderItems",careOrderItems);
                careOrderMap.put("allAmount",allAmount);
                careOrderList.add(careOrderMap);
            }

        }
        if(careOrderList.isEmpty()){
            return AjaxResult.fail("【"+regId+"】的挂号单没已支付的药品处方信息，请核对后再查询");
        }else{
            map.put("careOrders",careOrderList);
            return AjaxResult.success(map);
        }
    }

    /**
     * 发药
     * @return
     */
    @PostMapping("/doMedicine")
    public AjaxResult doMedicine(@RequestBody List<String> itemIds){
        //看库存，库存够就发药，不够就给予提示信息
        if(itemIds==null||itemIds.isEmpty()){
            return AjaxResult.fail("请选择要发药的药品项");
        }
        String msg=this.careService.doMedicine(itemIds);
        if(StringUtils.isBlank(msg)){
            return AjaxResult.success();
        }else{
            return AjaxResult.fail(msg);
        }
    }
}
