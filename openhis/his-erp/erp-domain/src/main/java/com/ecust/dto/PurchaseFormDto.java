package com.ecust.dto;

import com.ecust.domain.PurchaseItem;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(value="com-bjsxt-dto-PurchaseFromDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseFormDto extends BaseDto{
    //采购单主表数据
    private PurchaseDto purchaseDto;
    //采购详情表的数据
    private List<PurchaseItemDto> purchaseItemDtos;
}
