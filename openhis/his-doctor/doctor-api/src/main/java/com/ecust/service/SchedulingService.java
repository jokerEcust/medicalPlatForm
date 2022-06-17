package com.ecust.service;

import com.ecust.domain.Scheduling;
import com.ecust.domain.SimpleUser;
import com.ecust.dto.RegistrationQueryDto;
import com.ecust.dto.SchedulingFormQueryDto;
import com.ecust.dto.SchedulingQueryDto;

import java.util.List;

public interface SchedulingService {
    List<Scheduling> queryScheduling(SchedulingQueryDto schedulingQueryDto);

    int saveScheduling(SchedulingFormQueryDto schedulingFormQueryDto, SimpleUser simpleUser);

    List<Long> queryHasSchedulingDeptIds(RegistrationQueryDto registrationQueryDto);

}
