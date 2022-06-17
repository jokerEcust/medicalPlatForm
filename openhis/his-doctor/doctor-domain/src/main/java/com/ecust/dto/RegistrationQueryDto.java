package com.ecust.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value="com-ecust-dto-PatientDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationQueryDto extends BaseDto{
    private String schedulingDay;
    private Long deptId;
    private String schedulingType;
    private String subsectionType;
}
