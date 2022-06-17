package com.ecust.service;

import com.ecust.dto.InventoryLogDto;
import com.ecust.vo.DataGridView;

public interface InventoryLogService {
    DataGridView listForPage(InventoryLogDto inventoryLogDto);
}
