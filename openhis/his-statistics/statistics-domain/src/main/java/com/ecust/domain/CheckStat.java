package com.ecust.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckStat extends BaseEntity{
    private String checkItemName;
    private BigDecimal totalAmount;
    private Long checkItemId;
    private Integer count;

}
