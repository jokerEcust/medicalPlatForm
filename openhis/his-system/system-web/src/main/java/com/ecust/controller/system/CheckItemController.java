package com.ecust.controller.system;

import com.ecust.dto.CheckItemDto;
import com.ecust.service.CheckItemService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/system/checkItem")
public class CheckItemController {

    @Autowired
    CheckItemService checkItemService;

    @GetMapping("/listCheckItemForPage")
    public AjaxResult listCheckItemForPage(CheckItemDto checkItemDto){
        DataGridView dataGridView=checkItemService.listForPage(checkItemDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());

    }
    @PostMapping("/addCheckItem")
    public AjaxResult addCheckItem(@Valid CheckItemDto checkItemDto){
        checkItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(checkItemService.addCheckItem(checkItemDto));
    }

    @GetMapping("/getCheckItemById/{checkItemId}")
    public AjaxResult getCheckItemById(@PathVariable Long checkItemId){
        return AjaxResult.success(checkItemService.queryCheckItemById(checkItemId));
    }

    @PutMapping("/updateCheckItem")
    public AjaxResult updateCheckItem(@Valid CheckItemDto checkItemDto){
        checkItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(checkItemService.updateCheckItem(checkItemDto));
    }

    @DeleteMapping("/deleteCheckItemByIds/{checkItemId}")
    public AjaxResult deleteCheckItemByIds(@PathVariable Long checkItemId){
        return AjaxResult.toAjax(checkItemService.deleteCheckItemById(checkItemId));
    }

    @GetMapping("/selectAllCheckItem")
    public AjaxResult selectAllCheckItem(){
        return AjaxResult.success(checkItemService.queryAllCheckItem());
    }

}
