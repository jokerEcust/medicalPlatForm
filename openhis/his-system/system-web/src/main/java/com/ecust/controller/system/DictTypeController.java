package com.ecust.controller.system;

import com.ecust.aspectj.annotation.Log;
import com.ecust.aspectj.enums.BusinessStatus;
import com.ecust.aspectj.enums.BusinessType;
import com.ecust.domain.DictType;
import com.ecust.dto.DictTypeDto;
import com.ecust.service.DictTypeService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/system/dict/type")
public class DictTypeController {
    @Autowired
    DictTypeService dictTypeService;
    @GetMapping("/listForPage")
    public AjaxResult listForPage(DictTypeDto dictTypeDto){
        DataGridView dataGridView = dictTypeService.listPage(dictTypeDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }
    @PostMapping("/addDictType")
    @Log(title = "新增字典类型",businessType = BusinessType.INSERT)
    public AjaxResult addDictType(@Valid DictTypeDto dictTypeDto){
        if(dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(),dictTypeDto.getDictType())){
            return AjaxResult.fail("新增字典【" + dictTypeDto.getDictName() + "】失败，字典类型已存 在");
        }
        dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(dictTypeService.insert(dictTypeDto));

    }
    @GetMapping("/getOne/{dictId}")
    public AjaxResult getOneById(@PathVariable @Valid @NotNull(message = "字典id不可为空") Long dictId){
        DictType dictType = dictTypeService.selectDictTypeById(dictId);
        return AjaxResult.success("查询成功",dictType);
    }

    @PutMapping("/updateDictType")
    @Log(title = "修改字典类型",businessType = BusinessType.UPDATE)
    public AjaxResult updateDictType(@Valid DictTypeDto dictTypeDto){
        if(dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(),dictTypeDto.getDictType())){
            return AjaxResult.fail("修改字典【" + dictTypeDto.getDictName() + "】失败，字典类型已存在");
        }
        dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        int update = dictTypeService.update(dictTypeDto);
        return AjaxResult.toAjax(update);
    }
    @DeleteMapping("/deleteDictTypeByIds/{dictIds}")
    @Log(title = "删除字典类型",businessType = BusinessType.DELETE)
    public AjaxResult deleteDictTypeByIds(@PathVariable @Valid @NotEmpty(message = "待删除的id不可为空") Long[] dictIds){
        return AjaxResult.toAjax(dictTypeService.deleteDictTypeById(dictIds));
    }

    @GetMapping("/selectAllDictType")
    public AjaxResult selectAllDictType(){
        DataGridView list = dictTypeService.list();
        return AjaxResult.success("查询成功",list.getData());
    }

    @GetMapping("/dictCacheAsync")
    @Log(title = "缓存同步",businessType = BusinessType.UPDATE)
    public AjaxResult dictCacheAsync(){
        dictTypeService.dictCacheAsync();
        return AjaxResult.success();
    }

}
