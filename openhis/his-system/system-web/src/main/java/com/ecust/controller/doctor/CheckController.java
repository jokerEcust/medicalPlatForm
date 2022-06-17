package com.ecust.controller.doctor;

import cn.hutool.core.date.DateUtil;
import com.ecust.constants.Constants;
import com.ecust.controller.BaseController;
import com.ecust.domain.CareHistory;
import com.ecust.domain.CareOrder;
import com.ecust.domain.CareOrderItem;
import com.ecust.domain.CheckResult;
import com.ecust.dto.CheckResultDto;
import com.ecust.dto.CheckResultFormDto;
import com.ecust.service.CareService;
import com.ecust.service.CheckResultService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


@RequestMapping("/doctor/check")
@RestController
public class CheckController extends BaseController {

    @Reference
    private CareService careService;
    @Reference
    private CheckResultService checkResultService;

    @PostMapping("/queryNeedCheckItem")
    public AjaxResult queryNeedCheckItem(@RequestBody CheckResultDto checkResultDto) {
        List<CareOrderItem> resCareOrderItems = new ArrayList<>();
        if (StringUtils.isNotBlank(checkResultDto.getRegId())) {
            //如果regid存在
            CareHistory careHistory = this.careService.queryCareHistoryByRegId(checkResultDto.getRegId());
            if (null == careHistory) {
                return AjaxResult.success(resCareOrderItems);
            }
            //根据病例 ID 查询所有的处方信息
            List<CareOrder> careOrders = this.careService.queryCareOrdersByChId(careHistory.getChId());
            for (CareOrder careOrder : careOrders) {
                if (careOrder.getCoType().equals(Constants.CO_TYPE_CHECK)) {//只取检查处方
                    List<CareOrderItem> careOrderItems =
                            this.careService.queryCareOrderItemsByCoId(careOrder.getCoId(), Constants.ORDER_DETAILS_STATUS_1);
//过滤查询条件
                    for (CareOrderItem careOrderItem : careOrderItems) {
                        if (checkResultDto.getCheckItemIds().contains(Integer.valueOf(careOrderItem.getItemRefId()))) {
                            resCareOrderItems.add(careOrderItem);
                        }
                    }
                }
            }
            return AjaxResult.success(resCareOrderItems);
        } else {
            //查询所有已支付检查的项目
            List<CareOrderItem> careOrderItems = careService.queryCareOrderItemsByStatus(Constants.CO_TYPE_CHECK, Constants.ORDER_DETAILS_STATUS_1);
            for (CareOrderItem careOrderItem : careOrderItems) {
                if (checkResultDto.getCheckItemIds().contains(Integer.valueOf(careOrderItem.getItemRefId()))) {
                    resCareOrderItems.add(careOrderItem);
                }
            }
            return AjaxResult.success(resCareOrderItems);
        }
    }

    /**
     * 根据检查单号查询要检查的项目详情
     *
     * @param itemId
     * @return
     */
    @GetMapping("/queryCheckItemByItemId/{itemId}")
    public AjaxResult queryCheckItemByItemId(@PathVariable String itemId) {
        CareOrderItem careOrderItem = this.careService.queryCareOrderItemByItemId(itemId);
        if (careOrderItem == null) {
            return AjaxResult.fail("【" + itemId + "】的检查单号的数据不存在，请核对后再查询");
        }
        if (!careOrderItem.getStatus().equals(Constants.ORDER_DETAILS_STATUS_1)) {
            return AjaxResult.fail("【" + itemId + "】的检查单号的没有支付，请支付后再来检查");
        }
        if (!careOrderItem.getItemType().equals(Constants.CO_TYPE_CHECK)) {
            return AjaxResult.fail("【" + itemId + "】的单号不是检查项目，请核对后再查询");
        }
        CareOrder careOrder = this.careService.queryCareOrderByCoId(careOrderItem.getCoId());
        CareHistory careHistory = this.careService.queryCareHistoryByChId(careOrder.getChId());
        Map<String, Object> res = new HashMap<>();
        res.put("item", careOrderItem);
        res.put("careOrder", careOrder);
        res.put("careHistory", careHistory);
        return AjaxResult.success(res);
    }

    @PostMapping("startCheck/{itemId}")
    @HystrixCommand
    public AjaxResult startCheck(@PathVariable String itemId) {
        CareOrderItem careOrderItem = this.careService.queryCareOrderItemByItemId(itemId);
        if (careOrderItem == null) {
            return AjaxResult.fail("【" + itemId + "】的检查单号的数据不存在，请核对后再查询");
        }
        if (!careOrderItem.getStatus().equals(Constants.ORDER_DETAILS_STATUS_1)) {
            return AjaxResult.fail("【" + itemId + "】的检查单号的没有支付，请支付后再来检查");
        }
        if (!careOrderItem.getItemType().equals(Constants.CO_TYPE_CHECK)) {
            return AjaxResult.fail("【" + itemId + "】的单号不是检查项目，请核对后再查询");
        }
        CareOrder careOrder = this.careService.queryCareOrderByCoId(careOrderItem.getCoId());
        CareHistory careHistory = this.careService.queryCareHistoryByChId(careOrder.getChId());
        CheckResult checkResult = new CheckResult();
        checkResult.setItemId(itemId);
        checkResult.setCheckItemId(Integer.valueOf(careOrderItem.getItemRefId()));
        checkResult.setCheckItemName(careOrderItem.getItemName());
        checkResult.setPatientId(careOrder.getPatientId());
        checkResult.setPatientName(careOrder.getPatientName());
        checkResult.setPrice(careOrderItem.getPrice());
        checkResult.setRegId(careHistory.getRegId());
        checkResult.setResultStatus(Constants.RESULT_STATUS_0);//检查中
        checkResult.setCreateTime(DateUtil.date());
        checkResult.setCreateBy(ShiroSecurityUtils.getCurrentUserName());
        return AjaxResult.toAjax(checkResultService.saveCheckResult(checkResult));
    }

    /**
     * 分页查询所有检查中的项目
     */
    @PostMapping("queryAllCheckingResultForPage")
    public AjaxResult queryAllCheckingResultForPage(@RequestBody CheckResultDto checkResultDto) {
        checkResultDto.setResultStatus(Constants.RESULT_STATUS_0);//检查中的
        DataGridView dataGridView = this.checkResultService.queryAllCheckResultForPage(checkResultDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    /**
     * 完成检查
     *
     * @param checkResultFormDto 结果信息
     * @return
     */
    @PostMapping("/completeCheckResult")
    public AjaxResult completeCheckResult(@RequestBody @Valid CheckResultFormDto checkResultFormDto) {
        checkResultFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(checkResultService.completeCheckResult(checkResultFormDto));
    }

    @PostMapping("/queryAllCheckResultForPage")
    public AjaxResult queryAllCheckResultForPage(@RequestBody @Valid CheckResultDto checkResultDto) {
        //返回checkResult
        DataGridView dataGridView = this.checkResultService.queryAllCheckResultForPage(checkResultDto);
        HashSet<Integer> set=new HashSet();
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());

    }

    //还差一个文件上传的接口没有写

}

