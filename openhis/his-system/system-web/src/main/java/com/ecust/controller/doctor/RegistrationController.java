package com.ecust.controller.doctor;

import com.ecust.constants.Constants;
import com.ecust.controller.BaseController;
import com.ecust.domain.Dept;
import com.ecust.domain.Patient;
import com.ecust.domain.Registration;
import com.ecust.dto.PatientDto;
import com.ecust.dto.RegistrationDto;
import com.ecust.dto.RegistrationFormDto;
import com.ecust.dto.RegistrationQueryDto;
import com.ecust.service.DeptService;
import com.ecust.service.PatientService;
import com.ecust.service.RegistrationService;
import com.ecust.service.SchedulingService;
import com.ecust.utils.IdGeneratorSnowflake;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 患者挂号相关接口实现
 */
@RestController
@RequestMapping("/doctor/registration")
public class RegistrationController extends BaseController {
    @Reference
    PatientService patientService;

    @Reference
    RegistrationService registrationService;

    @Reference
    SchedulingService schedulingService;

    @Autowired
    DeptService deptService;

    /**
     * 分页查询出所有值班的科室
     * 1、在scheduling表中查询出所有的科室id
     * 2、在dept表中查询出对应的科室详细信息
     * @param registrationQueryDto
     * @return
     */
    @HystrixCommand
    @GetMapping("/listDeptForScheduling")
    public AjaxResult listDeptForScheduling(RegistrationQueryDto registrationQueryDto) {
        List<Long> deptIds = schedulingService.queryHasSchedulingDeptIds(registrationQueryDto);
        if (deptIds == null || deptIds.size() == 0) {
            //说明为空，返回一个空集合
            return AjaxResult.success(Collections.EMPTY_LIST);
        }
        List<Dept> list = deptService.listDeptByDeptIds(deptIds);
        return AjaxResult.success(list);

    }

    @HystrixCommand
    @GetMapping("/getPatientByIdCard/{idCard}")
    public AjaxResult getPatientByIdCard(@PathVariable String idCard) {
        Patient patient = patientService.queryPatientByIdCard(idCard);
        if (patient == null) {
            return AjaxResult.fail("身份证号【" + idCard + "】不存在，请先登记");
        }
        return AjaxResult.success(patient);
    }

    @HystrixCommand
    @PostMapping("/addRegistration")
    //不可在传参数的时候同时传递两个请求体数据
    public AjaxResult addRegistration(@RequestBody RegistrationFormDto registrationFormDto) {
        PatientDto patientDto = registrationFormDto.getPatientDto();
        RegistrationDto registrationDto = registrationFormDto.getRegistrationDto();
        //将患者信息封装成一个变量以供使用
        Patient patient = null;
        if (!StringUtils.isNotBlank(patientDto.getPatientId())) {
            //需要先登记
            patientDto.setPatientId(IdGeneratorSnowflake.generatorIdWithProfix("HZ"));
            patientDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
            //将患者信息存储到数据库中
            patient = patientService.addPatient(patientDto);
        } else {
            patient = patientService.getPatientById(patientDto.getPatientId());
        }
        //说明患者信息以完备
        //修改部门挂号数字（有人挂号，数值加一）
        Dept dept = deptService.getOne(registrationDto.getDeptId());
        //保存挂号信息
        registrationDto.setRegId(IdGeneratorSnowflake.generatorIdWithProfix("GH"));
        registrationDto.setPatientId(patient.getPatientId());
        registrationDto.setPatientName(patient.getName());
        registrationDto.setRegNumber(dept.getRegNumber() + 1);
        registrationDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        registrationService.addRegistration(registrationDto);
        //修改dept的挂号数量
        dept.setRegNumber(dept.getRegNumber() + 1);
        deptService.updateDeptRegNumber(dept.getDeptId(), dept.getRegNumber());
        return AjaxResult.success("", registrationDto.getRegId());
    }

    @HystrixCommand
    @PostMapping("/collectFee/{regId}")
    public AjaxResult collectFee(@PathVariable String regId) {
        //两个点：检查挂号单是否存在、检查挂号单的状态（未缴费状态）
        Registration registration = registrationService.queryRegistrationById(regId);
        if (registration == null) {
            return AjaxResult.fail("当前挂号单id不存在，请核对后操作");
        }
        if (!registration.getRegStatus().equals(Constants.REG_STATUS_0)) {
            //不是未收费的挂号单不能收费
            return AjaxResult.fail("当前挂号单不能收费");
        }
        //将挂号单的挂号状态改为待就诊
        registration.setRegStatus(Constants.REG_STATUS_1);
        return AjaxResult.toAjax(registrationService.updateRegistrationById(registration));

    }

    @GetMapping("/queryRegistrationForPage")
    public AjaxResult queryRegistrationForPage(RegistrationDto registrationDto) {
        DataGridView dataGridView = registrationService.listRegistrationForPage(registrationDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    /**
     * 挂号单作废
     *
     * @param regId
     * @return
     */
    @PostMapping("/doInvalid/{regId}")
    public AjaxResult doInvalid(@PathVariable String regId) {
        Registration registration = registrationService.queryRegistrationById(regId);
        if (registration == null) {
            return AjaxResult.fail("当前挂号单id不存在，请核对后操作");
        }
        if (!registration.getRegStatus().equals(Constants.REG_STATUS_0)) {
            //不是未收费的挂号单不能收费
            return AjaxResult.fail("当前挂号单不能作废");
        }
        //将挂号单的挂号状态改为待就诊
        registration.setRegStatus(Constants.REG_STATUS_5);
        return AjaxResult.toAjax(registrationService.updateRegistrationById(registration));
    }

    /**
     * 挂号单退号
     *
     * @param regId
     * @return
     */
    @PostMapping("/doReturn/{regId}")
    public AjaxResult doReturn(@PathVariable String regId) {
        Registration registration = registrationService.queryRegistrationById(regId);
        if (registration == null) {
            return AjaxResult.fail("当前挂号单id不存在，请核对后操作");
        }
        if (!registration.getRegStatus().equals(Constants.REG_STATUS_1)) {
            //不是未收费的挂号单不能收费
            return AjaxResult.fail("当前挂号单不能退号");
        }
        //将挂号单的挂号状态改为待就诊
        registration.setRegStatus(Constants.REG_STATUS_4);
        return AjaxResult.toAjax(registrationService.updateRegistrationById(registration));
    }
}
