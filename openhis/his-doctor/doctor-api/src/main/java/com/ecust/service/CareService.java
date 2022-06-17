package com.ecust.service;

import com.ecust.domain.CareHistory;
import com.ecust.domain.CareOrder;
import com.ecust.domain.CareOrderItem;
import com.ecust.dto.CareHistoryDto;
import com.ecust.dto.CareOrderFormDto;

import java.util.List;

public interface CareService {
    List<CareHistory> getCareHistoryByPatientId(String patientId);


    CareHistory saveOrUpdateCareHistory(CareHistoryDto careHistoryDto);

    CareHistory queryCareHistoryByRegId(String regId);

    List<CareOrder> queryCareOrdersByChId(String chId);

    List<CareOrderItem> queryCareOrderItemsByCoId(String coId);

    List<CareOrderItem> queryCareOrderItemsByCoId(String coId, String status);

    CareHistory queryCareHistoryByChId(String chId);

    int saveCareOrderItem(CareOrderFormDto careOrderFormDto);

    CareOrderItem queryCareOrderItemByItemId(String itemId);

    int deleteCareOrderItemByItemId(String itemId);

    int visitComplete(String regId);

    String doMedicine(List<String> itemIds);

    List<CareOrderItem> queryCareOrderItemsByStatus(String coType, String status);

    CareOrder queryCareOrderByCoId(String coId);
}
