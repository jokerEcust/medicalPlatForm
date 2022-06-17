package com.ecust.service;

import com.ecust.domain.Provider;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.dto.ProviderDto;
import com.ecust.vo.DataGridView;

import java.util.List;

public interface ProviderService{


    DataGridView listForPage(ProviderDto providerDto);

    int addProvider(ProviderDto providerDto);

    Provider queryProviderById(Long providerId);

    int updateProvider(ProviderDto providerDto);

    int deleteProviderByIds(Long[] providerIds);

    List<Provider> queryAllProvider();
}
