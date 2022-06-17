package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.domain.PatientFile;
import com.ecust.dto.PatientDto;
import com.ecust.mapper.PatientFileMapper;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.ecust.domain.Patient;
import com.ecust.mapper.PatientMapper;
import com.ecust.service.PatientService;
@Service
public class PatientServiceImpl implements PatientService{

    @Autowired
    PatientMapper patientMapper;

    @Autowired
    PatientFileMapper patientFileMapper;
    @Override
    public DataGridView listPagePatient(PatientDto patientDto) {
        Page<Patient> page=new Page<>(patientDto.getPageNum(),patientDto.getPageSize());
        QueryWrapper<Patient> qw=new QueryWrapper<>();
        qw.like(StringUtils.isNoneBlank(patientDto.getIdCard()),Patient.COL_ID_CARD,patientDto.getIdCard());
        qw.like(StringUtils.isNoneBlank(patientDto.getName()),Patient.COL_NAME,patientDto.getName());
        qw.like(StringUtils.isNoneBlank(patientDto.getPhone()),Patient.COL_PHONE,patientDto.getPhone());
        patientMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public Patient queryPatientById(String patientId) {
        if (patientId!= null){
            return patientMapper.selectById(patientId);
        }
        return null;
    }


    @Override
    public PatientFile queryPatientFileById(String patientId) {
        if (patientId!= null){
            return patientFileMapper.selectById(patientId);
        }
        return null;
    }

    @Override
    public Patient queryPatientByIdCard(String idCard) {
        if (idCard!= null){
            QueryWrapper<Patient> qw=new QueryWrapper<>();
            qw.like(Patient.COL_ID_CARD,idCard);
            return patientMapper.selectOne(qw);
        }
        return null;
    }

    @Override
    public Patient addPatient(PatientDto patientDto) {
        Patient patient = new Patient();
        BeanUtil.copyProperties(patientDto,patient);
        int i = patientMapper.insert(patient);
        if (i==1){
            return patient;
        }
        return null;

    }

    @Override
    public Patient getPatientById(String patientId) {
        if (patientId==null){
            return null;
        }
        QueryWrapper<Patient> qw=new QueryWrapper<>();
        qw.eq(Patient.COL_PATIENT_ID,patientId);
        return patientMapper.selectOne(qw);
    }

    @Override
    public PatientFile getPatienFiletById(String patientId) {
        if (patientId==null){
            return null;
        }
        QueryWrapper<PatientFile> qw=new QueryWrapper<>();
        qw.eq(PatientFile.COL_PATIENT_ID,patientId);
        return patientFileMapper.selectOne(qw);
    }
}
