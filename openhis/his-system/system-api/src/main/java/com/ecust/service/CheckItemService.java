package com.ecust.service;

import com.ecust.domain.CheckItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.dto.CheckItemDto;
import com.ecust.vo.DataGridView;

import java.util.List;

public interface CheckItemService{


    DataGridView listForPage(CheckItemDto checkItemDto);

    int addCheckItem(CheckItemDto checkItemDto);

    CheckItem queryCheckItemById(Long checkItemId);

    int updateCheckItem(CheckItemDto checkItemDto);

    int deleteCheckItemById(Long checkItemId);

    List<CheckItem> queryAllCheckItem();
}
