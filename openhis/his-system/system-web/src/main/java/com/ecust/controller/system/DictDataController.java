package com.ecust.controller.system;

import com.ecust.aspectj.annotation.Log;
import com.ecust.aspectj.enums.BusinessType;
import com.ecust.domain.DictData;
import com.ecust.dto.DictDataDto;
import com.ecust.service.DictDataService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/system/dict/data")
public class DictDataController {
    @Autowired
    DictDataService dictDataService;
    @GetMapping("/listForPage")
    public AjaxResult listForPage(DictDataDto dictDataDto){
        DataGridView dataGridView = dictDataService.listPage(dictDataDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }
    @PostMapping("/addDictData")
    @Log(title = "新增字典数据",businessType = BusinessType.INSERT)
    public AjaxResult addDictData(@Valid DictDataDto dictDataDto){
        dictDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(dictDataService.insert(dictDataDto));

    }
    @GetMapping("/getOne/{dictCode}")
    public AjaxResult getOneById(@PathVariable @Valid @NotNull(message = "字典id不可为空") Long dictCode){
        DictData dictData = dictDataService.selectDictDataById(dictCode);
        return AjaxResult.success("查询成功",dictData);
    }

    @PutMapping("/updateDictData")
    @Log(title = "更新字典数据",businessType = BusinessType.UPDATE)
    public AjaxResult updateDictData(@Valid DictDataDto dictDataDto){
        dictDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        int update = dictDataService.update(dictDataDto);
        return AjaxResult.toAjax(update);
    }
    @DeleteMapping("/deleteDictDataByIds/{dictCodeIds}")
    @Log(title = "删除字典数据",businessType = BusinessType.DELETE)
    public AjaxResult deleteDictDataByIds(@PathVariable @Valid @NotEmpty(message = "待删除的id不可为空") Long[] dictCodeIds){
        return AjaxResult.toAjax(dictDataService.deleteDictDataByIds(dictCodeIds));
    }

    @GetMapping("/getDataByType/{dictType}")
    public AjaxResult selectAllDictData(@PathVariable @Valid @NotEmpty(message = "字典数据不可为空") String dictType){
        List<DictData> dictData = dictDataService.selectDictDataByDictType(dictType);
        return AjaxResult.success("查询成功",dictData);
    }


}
