package com.ecust.controller.rep;

import com.ecust.constants.Constants;
import com.ecust.controller.BaseController;
import com.ecust.domain.Purchase;
import com.ecust.domain.PurchaseItem;
import com.ecust.dto.PurchaseDto;
import com.ecust.dto.PurchaseFormDto;
import com.ecust.service.PurchaseService;
import com.ecust.utils.IdGeneratorSnowflake;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.dubbo.config.annotation.Reference;
import org.aspectj.weaver.AnnotationAJ;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/erp/purchase")
public class PurchaseController extends BaseController {
    @Reference
    PurchaseService purchaseService;

    @GetMapping("/listPurchaseForPage")
    @HystrixCommand
    public AjaxResult listPurchaseForPage(PurchaseDto purchaseDto) {
        System.out.println("PurchaseController.listPurchaseForPage");
        DataGridView dataGridView = purchaseService.listForPage(purchaseDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());

    }

    /**
     * 提交审核（只有状态满足条件的时候才能提交审核）
     *
     * @param purchaseId 采购单号
     * @return
     */
    @PostMapping("/doAudit/{purchaseId}")
    @HystrixCommand
    public AjaxResult doAudit(@PathVariable String purchaseId) {
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        //只能对未提交或审核失败的单据提交
        if (purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_1) || purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_4)) {
            return AjaxResult.toAjax(purchaseService.doAudit(purchaseId, ShiroSecurityUtils.getCurrentSimpleUser()));
        } else {
            return AjaxResult.fail("当前单据状态不是【未提交】或【审核失败】状态，不能提交审核");
        }
    }

    /**
     * 作废采购（只有状态满足条件的时候才能作废）
     *
     * @param purchaseId 采购单号
     * @return
     */
    @HystrixCommand
    @PostMapping("/doInvalid/{purchaseId}")
    public AjaxResult doInvalid(@PathVariable String purchaseId) {
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_4)||purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_1)) {
            return AjaxResult.toAjax(purchaseService.doInvalid(purchaseId));
        } else {
            return AjaxResult.fail("当前单据状态不是【待审核】状态，不能作废审核");
        }
    }

    /**
     * 分页查询所有待审核的入库信息
     *
     * @param purchaseDto
     * @return
     */
    @HystrixCommand
    @GetMapping("/listPurchasePendingForPage")
    public AjaxResult listPurchasePendingForPage(@Valid PurchaseDto purchaseDto) {
        purchaseDto.setStatus(Constants.STOCK_PURCHASE_STATUS_2);
        DataGridView dataGridView = purchaseService.listForPage(purchaseDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    /**
     * 审核通过接口
     * @param purchaseId
     * @return
     */

    @HystrixCommand
    @PostMapping("/auditPass/{purchaseId}")
    public AjaxResult auditPass(@PathVariable String purchaseId) {
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_2)) {
            return AjaxResult.toAjax(purchaseService.auditPass(purchaseId));
        } else {
            return AjaxResult.fail("当前单据状态不是【待审核】状态，不能通过审核");
        }
    }

    /**
     * 审核不通过接口
     * @param purchaseId
     * @param auditMsg
     * @return
     */
    @HystrixCommand
    @PostMapping("/auditNoPass/{purchaseId}/{auditMsg}")
    public AjaxResult auditNoPass(@PathVariable String purchaseId, @PathVariable String auditMsg) {
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_2)) {
            return AjaxResult.toAjax(purchaseService.auditNoPass(purchaseId, auditMsg));
        } else {
            return AjaxResult.fail("当前单据状态不是【待审核】状态");
        }
    }

    @GetMapping("/getPurchaseItemById/{purchaseId}")
    public AjaxResult getPurchaseItemById(@PathVariable String purchaseId) {
        List<PurchaseItem> items = purchaseService.getPurchaseItemById(purchaseId);
        return AjaxResult.success(items);
    }

    //暂存采购单信息
    @PostMapping("/addPurchase")
    public AjaxResult addPurchase(@RequestBody PurchaseFormDto purchaseFormDto) {
        //暂存过：
        if (!checkPurchase(purchaseFormDto)){
            return AjaxResult.fail("当前单据状态不是【未提交】或【审核失败】状态，不能进行修改");
        }
        purchaseFormDto.getPurchaseDto().setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.purchaseService.addPurchaseAndItem(purchaseFormDto));

    }

    //验证数据表单暂存状态是否可以修改
    //fasle:代表不可修改（存在不满足状态）  true：代表可修改（存在且满足状态或者不存在）
    private boolean checkPurchase(PurchaseFormDto purchaseFormDto) {
        String purchaseId = purchaseFormDto.getPurchaseDto().getPurchaseId();
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        if (purchase != null && (!purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_1) && !purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_4))) {
            //说明数据库中存在这个数据，但不满足状态
            return false;
        }
        return true;

    }

    /**
     * 添加购物单并提交审核
     * @param purchaseFormDto
     * @return
     */
    @PostMapping("/addPurchaseToAudit")
    public AjaxResult addPurchaseToAudit(@RequestBody PurchaseFormDto purchaseFormDto){
        if (!checkPurchase(purchaseFormDto)){
            return AjaxResult.fail("当前单据状态不是【未提交】或【审核失败】状态，不能进行修改");
        }
        purchaseFormDto.getPurchaseDto().setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.purchaseService.addPurchaseAndItemToAudit(purchaseFormDto));
    }

    @GetMapping("/generatePurchaseId")
    public AjaxResult generatePurchaseId(){
        return AjaxResult.success(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_CG));
    }

    /**
     * 根据采购单号查询采购单信息与详情信息
     * @param purchaseId
     * @return
     */
    @GetMapping("/queryPurchaseAndItemByPurchaseId/{purchaseId}")
    public AjaxResult queryPurchaseAndItemByPurchaseId(@PathVariable String purchaseId){
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        if(purchase==null){
            return AjaxResult.fail("单据号【"+purchaseId+"】不存在");
        }
        List<PurchaseItem> list=purchaseService.getPurchaseItemById(purchaseId);
        Map<String,Object> res=new HashMap<>();
        res.put("purchase",purchase);
        res.put("items",list);
        return AjaxResult.success(res);

    }

    /**
     * 入库操作
     * @param purchaseId
     * @return
     */
    @PostMapping("/doInventory/{purchaseId}")
    public AjaxResult doInventory(@PathVariable String purchaseId){
        Purchase purchase = purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_3)){
            //审核通过
            return AjaxResult.toAjax(purchaseService.doInventory(purchaseId,ShiroSecurityUtils.getCurrentSimpleUser()));
        }else if(purchase.getStatus().equals(Constants.STOCK_PURCHASE_STATUS_6)){
            return AjaxResult.fail("采购单【" + purchaseId + "】已入库，不能重复入库");
        }else {
            return AjaxResult.fail("采购单【" + purchaseId + "】没有审核通过，不能入库");
        }
    }

}
