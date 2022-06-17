package com.ecust.controller.system;

import com.ecust.dto.OperLogDto;
import com.ecust.service.OperLogService;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/system/operLog")
public class OperLogController {
    @Autowired
    OperLogService operLogService;

    @GetMapping("/listForPage")
    public AjaxResult listForPage(OperLogDto operLogDto) {
        DataGridView dataGridView = operLogService.listForPage(operLogDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    @DeleteMapping("/deleteOperLogByIds/{infoIds}")
    public AjaxResult deleteOperLogByIds(@PathVariable Long[] infoIds) {
        return AjaxResult.toAjax(operLogService.deleteOperLogByIds(infoIds));
    }

    @DeleteMapping("/clearAllOperLog")
    public AjaxResult clearAllOperLog() {
        return AjaxResult.toAjax(operLogService.clearAllOperLog());
    }

}
