package com.ecust.service;

import com.ecust.domain.Registration;
import com.ecust.dto.RegistrationDto;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public interface RegistrationService {


    void addRegistration(RegistrationDto registrationDto);

    Registration queryRegistrationById(String regId);

    int updateRegistrationById(Registration registration);

    DataGridView listRegistrationForPage(RegistrationDto registrationDto);

    List<Registration> queryRegistration(Long deptId, String scheudlingType, String subSectionType, String regStatus, Long userId);

}