package com.ecust.controller.rep;

import com.ecust.controller.BaseController;
import com.ecust.dto.MedicinesDto;
import com.ecust.service.MedicinesService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/erp/medicines")
public class MedicinesController extends BaseController {
    @Reference
    MedicinesService medicinesService;

    @GetMapping("/listMedicinesForPage")
    @HystrixCommand
    public AjaxResult listMedicinesForPage(MedicinesDto medicinesDto) {
        DataGridView dataGridView = medicinesService.listForPage(medicinesDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());

    }

    @PostMapping("/addMedicines")
    @HystrixCommand
    public AjaxResult addMedicinesDto(@Valid MedicinesDto medicinesDto) {
        medicinesDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(medicinesService.addMedicines(medicinesDto));
    }

    @HystrixCommand
    @GetMapping("/getMedicinesById/{medicinesId}")
    public AjaxResult getMedicinesById(@PathVariable Long medicinesId) {
        return AjaxResult.success(medicinesService.queryMedicinesById(medicinesId));
    }

    @HystrixCommand
    @PutMapping("/updateMedicines")
    public AjaxResult updateMedicines(@Valid MedicinesDto medicinesDto) {
        medicinesDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(medicinesService.updateMedicines(medicinesDto));
    }

    @HystrixCommand
    @DeleteMapping("/deleteMedicinesByIds/{medicinesIds}")
    public AjaxResult deleteMedicinesByIds(@PathVariable Long[] medicinesIds) {
        return AjaxResult.toAjax(medicinesService.deleteMedicinesByIds(medicinesIds));
    }

    @HystrixCommand
    @GetMapping("/selectAllMedicines")
    public AjaxResult selectAllMedicines() {
        return AjaxResult.success(medicinesService.queryAllMedicines());
//        return null;
    }

    @HystrixCommand
    @PostMapping("/updateMedicinesStorage/{medicinesId}/{medicinesStockNum}")
    public AjaxResult updateMedicinesStorage(@PathVariable Long medicinesId, @PathVariable Long medicinesStockNum) {
        return AjaxResult.toAjax(medicinesService.updateMedicinesStorage(medicinesId, medicinesStockNum));
    }


}
