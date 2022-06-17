package com.ecust.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Drug extends BaseEntity{
    private String medicinesId;
    private String medicinesName;
    private Double price;
    private Integer num;
    private Double amount;
    private String createTime;
}
