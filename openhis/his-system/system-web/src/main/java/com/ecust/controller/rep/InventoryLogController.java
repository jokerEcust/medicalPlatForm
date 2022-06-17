package com.ecust.controller.rep;

import com.ecust.controller.BaseController;
import com.ecust.dto.InventoryLogDto;
import com.ecust.service.InventoryLogService;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/erp/inventoryLog")
public class InventoryLogController extends BaseController {
    @Reference
    InventoryLogService inventoryLogService;
    @GetMapping("/listInventoryLogForPage")
    public AjaxResult listInventoryLogForPage(InventoryLogDto inventoryLogDto){
        DataGridView dataGridView=inventoryLogService.listForPage(inventoryLogDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }
}


