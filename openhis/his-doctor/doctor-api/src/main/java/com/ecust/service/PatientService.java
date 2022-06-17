package com.ecust.service;

import com.ecust.domain.Patient;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.domain.PatientFile;
import com.ecust.dto.PatientDto;
import com.ecust.vo.DataGridView;

public interface PatientService{


    DataGridView listPagePatient(PatientDto patientDto);

    Patient queryPatientById(String patientId);

    PatientFile queryPatientFileById(String patientId);

    Patient queryPatientByIdCard(String idCard);

    Patient addPatient(PatientDto patientDto);

    Patient getPatientById(String patientId);

    PatientFile getPatienFiletById(String patientId);
}
