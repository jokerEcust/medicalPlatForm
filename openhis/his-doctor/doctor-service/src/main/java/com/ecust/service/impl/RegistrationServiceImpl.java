package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.constants.Constants;
import com.ecust.domain.Registration;
import com.ecust.dto.RegistrationDto;
import com.ecust.mapper.RegistrationMapper;
import com.ecust.service.RegistrationService;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    RegistrationMapper registrationMapper;

    @Override
    public void addRegistration(RegistrationDto registrationDto) {
        Registration registration = new Registration();
        BeanUtil.copyProperties(registrationDto, registration);
        registration.setCreateBy(registrationDto.getSimpleUser().getUserName());
        registration.setCreateTime(DateUtil.date());
        registration.setRegStatus(Constants.REG_STATUS_0);
        registrationMapper.insert(registration);
    }

    @Override
    public Registration queryRegistrationById(String regId) {
        if (regId == null) {
            return null;
        }
        QueryWrapper<Registration> qw=new QueryWrapper<>();
        qw.eq(Registration.COL_REG_ID,regId);
        return registrationMapper.selectOne(qw);
    }

    @Override
    public int updateRegistrationById(Registration registration) {
        return registrationMapper.updateById(registration);
    }

    @Override
    public DataGridView listRegistrationForPage(RegistrationDto registrationDto) {
        Page<Registration> page = new Page<>(registrationDto.getPageNum(), registrationDto.getPageSize());
        QueryWrapper<Registration> qw = new QueryWrapper<>();
        qw.eq(registrationDto.getDeptId()!=null, Registration.COL_DEPT_ID, registrationDto.getDeptId());
        qw.eq(StringUtils.isNotBlank(registrationDto.getRegStatus()), Registration.COL_REG_STATUS, registrationDto.getRegStatus());
        qw.eq(StringUtils.isNotBlank(registrationDto.getPatientName()), Registration.COL_PATIENT_NAME, registrationDto.getPatientName());
        qw.eq(StringUtils.isNotBlank(registrationDto.getSchedulingType()), Registration.COL_SCHEDULING_TYPE, registrationDto.getSchedulingType());
        qw.eq(StringUtils.isNotBlank(registrationDto.getSubsectionType()), Registration.COL_SUBSECTION_TYPE, registrationDto.getSubsectionType());
        qw.eq(StringUtils.isNotBlank(registrationDto.getVisitDate()), Registration.COL_VISIT_DATE, registrationDto.getVisitDate());
        registrationMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());

    }

    @Override
    public List<Registration> queryRegistration(Long deptId, String scheudlingType, String subSectionType, String regStatus, Long userId) {
        QueryWrapper<Registration> qw=new QueryWrapper<>();
        qw.eq(Registration.COL_DEPT_ID,deptId);
        qw.eq(Registration.COL_SCHEDULING_TYPE,scheudlingType);
        qw.eq(StringUtils.isNotBlank(subSectionType),Registration.COL_SUBSECTION_TYPE,subSectionType);
        qw.eq(Registration.COL_REG_STATUS,regStatus);
        qw.eq(userId!=null,Registration.COL_USER_ID,userId);
        qw.orderByAsc(Registration.COL_REG_NUMBER);
        return registrationMapper.selectList(qw);

    }
}
