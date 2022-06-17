package com.ecust.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugStat{
    private String medicinesId;
    private String medicinesName;
    private Double totalAmount;
    private int count;
}
