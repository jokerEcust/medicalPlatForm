package com.ecust.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Check extends BaseEntity {
    private String checkItemId;
    private String checkItemName;
    private Double price;
    private String patientId;
    private String patientName;
    private String resultStatus;
    private String createTime;
    private String createBy;
}
