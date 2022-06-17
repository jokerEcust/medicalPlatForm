package com.ecust.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@ApiModel(value="com-ecust-dto-RegistrationFormDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationFormDto extends BaseDto {
    private PatientDto patientDto;
    private RegistrationDto registrationDto;
}
