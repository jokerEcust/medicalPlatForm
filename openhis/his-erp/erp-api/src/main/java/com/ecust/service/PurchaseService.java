package com.ecust.service;

import com.ecust.domain.Purchase;
import com.ecust.domain.PurchaseItem;
import com.ecust.domain.SimpleUser;
import com.ecust.dto.PurchaseDto;
import com.ecust.dto.PurchaseFormDto;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public interface PurchaseService{


    DataGridView listForPage(PurchaseDto purchaseDto);

    int doAudit(String purchaseId, SimpleUser simpleUser);

    int doInvalid(String purchaseId);

    int auditPass(String purchaseId);

    int auditNoPass(String purchaseId, String auditMsg);

    Purchase getPurchaseById(String purchaseId);

    List<PurchaseItem> getPurchaseItemById(String purchaseId);

    int addPurchaseAndItem(PurchaseFormDto purchaseFormDto);

    int addPurchaseAndItemToAudit(PurchaseFormDto purchaseFormDto);

    int doInventory(String purchaseId, SimpleUser simpleUser);
}
