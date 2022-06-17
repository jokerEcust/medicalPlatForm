package com.ecust.service;

import com.ecust.domain.Producter;
import com.ecust.dto.ProducterDto;
import com.ecust.vo.DataGridView;

import java.util.List;

public interface ProducterService{


    DataGridView listForPage(ProducterDto producterDto);

    int addProducter(ProducterDto producterDto);

    Producter queryProducterById(Long producterId);

    int updateProducter(ProducterDto producterDto);

    int deleteProducterByIds(Long[] producterIds);

    List<Producter> queryAllProducter();

}
