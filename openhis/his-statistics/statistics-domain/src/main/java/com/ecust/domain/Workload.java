package com.ecust.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class Workload extends BaseEntity {
    private String regId;
    private String userId;
    private String doctorName;
    private BigDecimal regAmount;
    private String patientName;
    private Date visitDate;
}
