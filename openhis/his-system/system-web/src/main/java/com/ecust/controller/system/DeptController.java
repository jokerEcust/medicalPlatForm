package com.ecust.controller.system;

import com.ecust.aspectj.annotation.Log;
import com.ecust.aspectj.enums.BusinessType;
import com.ecust.domain.Dept;
import com.ecust.dto.DeptDto;
import com.ecust.service.DeptService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/system/dept")
public class DeptController {
    @Autowired
    DeptService deptService;
    @GetMapping("/listDeptForPage")
    public AjaxResult listDeptForPage(DeptDto deptDto){
        DataGridView dataGridView = deptService.listForPage(deptDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }
    @PostMapping("/addDept")
    @Log(title = "新增科室信息",businessType = BusinessType.INSERT)
    public AjaxResult addDictData(@Valid DeptDto deptDto){
        deptDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(deptService.addDept(deptDto));

    }
    @GetMapping("/getDeptById/{deptId}")
    public AjaxResult getOneById(@PathVariable @Valid @NotNull(message = "id不可为空") Long deptId){
        Dept dept = deptService.getOne(deptId);
        return AjaxResult.success("查询成功",dept);
    }

    @PutMapping("/updateDept")
    @Log(title = "更新科室信息",businessType = BusinessType.UPDATE)
    public AjaxResult updateDictData(@Valid DeptDto deptDto){
        deptDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        int update = deptService.updateDept(deptDto);
        return AjaxResult.toAjax(update);
    }
    @DeleteMapping("/deleteDeptByIds/{deptIds}")
    @Log(title = "删除科室信息",businessType = BusinessType.DELETE)
    public AjaxResult deleteDictDataByIds(@PathVariable @Valid @NotEmpty(message = "待删除的id不可为空") Long[] deptIds){
        return AjaxResult.toAjax(deptService.deleteDeptByIds(deptIds));
    }

    @GetMapping("/selectAllDept")
    public AjaxResult selectAllDictData(){
        return AjaxResult.success("查询成功",deptService.list());
    }


}
