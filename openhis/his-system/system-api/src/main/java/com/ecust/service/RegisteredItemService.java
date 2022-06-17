package com.ecust.service;

import com.ecust.domain.RegisteredItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.dto.RegisteredItemDto;
import com.ecust.vo.DataGridView;

import java.util.List;

public interface RegisteredItemService{


    DataGridView listForPage(RegisteredItemDto registeredItemDto);

    int addRegisteredItem(RegisteredItemDto registeredItemDto);

    RegisteredItem queryRegisteredItemById(Long registeredItemId);

    int updateRegisteredItem(RegisteredItemDto registeredItemDto);

    int deleteRegisteredItemById(Long regItemId);

    List<RegisteredItem> queryAllRegisteredItem();
}
