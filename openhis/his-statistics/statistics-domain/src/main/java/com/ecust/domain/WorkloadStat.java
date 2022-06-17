package com.ecust.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkloadStat extends BaseEntity {
    private String userId;
    private String doctorName;
    private BigDecimal totalAmount;
    private Long count;
}
