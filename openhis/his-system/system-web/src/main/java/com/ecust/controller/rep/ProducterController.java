package com.ecust.controller.rep;

import com.ecust.controller.BaseController;
import com.ecust.dto.ProducterDto;
import com.ecust.service.ProducterService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/erp/producter")
public class ProducterController extends BaseController {
    @Reference
    ProducterService producterService;

    @GetMapping("/listProducterForPage")
    @HystrixCommand
    public AjaxResult listProducterForPage(ProducterDto producterDto){
        DataGridView dataGridView=producterService.listForPage(producterDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());

    }
    @PostMapping("/addProducter")
    @HystrixCommand
    public AjaxResult addProducterDto(@Valid ProducterDto producterDto){
        producterDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(producterService.addProducter(producterDto));
    }
    @HystrixCommand
    @GetMapping("/getProducterById/{producterId}")
    public AjaxResult getProducterById(@PathVariable Long producterId){
        return AjaxResult.success(producterService.queryProducterById(producterId));
    }
    @HystrixCommand
    @PutMapping("/updateProducter")
    public AjaxResult updateProducter(@Valid ProducterDto producterDto){
        producterDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(producterService.updateProducter(producterDto));
    }
    @HystrixCommand
    @DeleteMapping("/deleteProducterByIds/{producterIds}")
    public AjaxResult deleteProducterByIds(@PathVariable Long[] producterIds){
        return AjaxResult.toAjax(producterService.deleteProducterByIds(producterIds));
    }
    @HystrixCommand
    @GetMapping("/selectAllProducter")
    public AjaxResult selectAllProducter(){
        return AjaxResult.success(producterService.queryAllProducter());
    }

}
