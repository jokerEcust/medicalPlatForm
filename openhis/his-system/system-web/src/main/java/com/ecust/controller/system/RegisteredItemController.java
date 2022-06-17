package com.ecust.controller.system;

import com.ecust.dto.RegisteredItemDto;
import com.ecust.service.RegisteredItemService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/system/registeredItem")
public class RegisteredItemController {

    @Autowired
    RegisteredItemService registeredItemService;

    @GetMapping("/listRegisteredItemForPage")
    public AjaxResult listRegisteredItemForPage(RegisteredItemDto registeredItemDto){
        DataGridView dataGridView=registeredItemService.listForPage(registeredItemDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());

    }
    @PostMapping("/addRegisteredItem")
    public AjaxResult addRegisteredItem(@Valid RegisteredItemDto registeredItemDto){
        registeredItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(registeredItemService.addRegisteredItem(registeredItemDto));
    }

    @GetMapping("/getRegisteredItemById/{registeredItemId}")
    public AjaxResult getRegisteredItemById(@PathVariable Long registeredItemId){
        return AjaxResult.success(registeredItemService.queryRegisteredItemById(registeredItemId));
    }

    @PutMapping("/updateRegisteredItem")
    public AjaxResult updateRegisteredItem(@Valid RegisteredItemDto registeredItemDto){
        registeredItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(registeredItemService.updateRegisteredItem(registeredItemDto));
    }

    @DeleteMapping("/deleteRegisteredItemByIds/{regItemId}")
    public AjaxResult deleteRegisteredItemByIds(@PathVariable Long regItemId){
        return AjaxResult.toAjax(registeredItemService.deleteRegisteredItemById(regItemId));
    }

    @GetMapping("/selectAllRegisteredItem")
    public AjaxResult selectAllRegisteredItem(){
        return AjaxResult.success(registeredItemService.queryAllRegisteredItem());
    }

}
