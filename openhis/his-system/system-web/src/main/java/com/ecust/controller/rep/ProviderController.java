package com.ecust.controller.rep;

import com.ecust.controller.BaseController;
import com.ecust.dto.ProviderDto;
import com.ecust.service.ProviderService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/erp/provider")
public class ProviderController extends BaseController {
    @Reference
    ProviderService providerService;

    @GetMapping("/listProviderForPage")
    @HystrixCommand
    public AjaxResult listProviderForPage(ProviderDto providerDto){
        DataGridView dataGridView=providerService.listForPage(providerDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());

    }
    @PostMapping("/addProvider")
    @HystrixCommand
    public AjaxResult addProviderDto(@Valid ProviderDto providerDto){
        providerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(providerService.addProvider(providerDto));
    }
    @HystrixCommand
    @GetMapping("/getProviderById/{providerId}")
    public AjaxResult getProviderById(@PathVariable Long providerId){
        return AjaxResult.success(providerService.queryProviderById(providerId));
    }
    @HystrixCommand
    @PutMapping("/updateProvider")
    public AjaxResult updateProvider(@Valid ProviderDto providerDto){
        providerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(providerService.updateProvider(providerDto));
    }
    @HystrixCommand
    @DeleteMapping("/deleteProviderByIds/{providerIds}")
    public AjaxResult deleteProviderByIds(@PathVariable Long[] providerIds){
        return AjaxResult.toAjax(providerService.deleteProviderByIds(providerIds));
    }
    @HystrixCommand
    @GetMapping("/selectAllProvider")
    public AjaxResult selectAllProvider(){
        return AjaxResult.success(providerService.queryAllProvider());
    }

}
