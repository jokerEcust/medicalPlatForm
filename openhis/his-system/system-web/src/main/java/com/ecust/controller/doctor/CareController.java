package com.ecust.controller.doctor;

import cn.hutool.core.date.DateUtil;
import com.ecust.constants.Constants;
import com.ecust.controller.BaseController;
import com.ecust.domain.*;
import com.ecust.dto.CareHistoryDto;
import com.ecust.dto.CareOrderFormDto;
import com.ecust.service.CareService;
import com.ecust.service.DeptService;
import com.ecust.service.PatientService;
import com.ecust.service.RegistrationService;
import com.ecust.utils.HisDateUtils;
import com.ecust.utils.IdGeneratorSnowflake;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/doctor/care")
public class CareController extends BaseController {
    @Autowired
    DeptService deptService;
    @Reference
    RegistrationService registrationService;

    @Reference
    PatientService patientService;

    @Reference
    CareService careService;

    /**
     * ：查询待就诊的挂号信息
     * doctor/care/queryToBeSeenRegistration/{scheudlingType}
     * 1、挂号单类型为待就诊
     * 2、医生排班日期吻合
     * 3、科室类型相同（都是外科）
     * 4、诊室类型相同（都是门诊）
     */
    @GetMapping("/queryToBeSeenRegistration/{scheudlingType}")
    public AjaxResult queryToBeSeenRegistration(@PathVariable String scheudlingType) {
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        String regStatus = Constants.REG_STATUS_1;
        String subSectionType = HisDateUtils.getCurrentTimeType();
        //获取医生id是为了避免不同的医生接诊同一位患者，主要是查询就诊中和就诊完成的时候不用重写查询方法
        Long userId = null;
        List<Registration> list = registrationService.queryRegistration(deptId, scheudlingType, subSectionType, regStatus, userId);
        return AjaxResult.success(list);

    }

    /**
     * 查询就诊中的挂号信息
     * doctor/care/queryVisitingRegistration/{scheudlingType}
     */
    @GetMapping("/queryVisitingRegistration/{scheudlingType}")
    public AjaxResult queryVisitingRegistration(@PathVariable String scheudlingType) {
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        String regStatus = Constants.REG_STATUS_2;
        String subSectionType = null;
        Long userId = ShiroSecurityUtils.getCurrentUser().getUserId();
        List<Registration> list = registrationService.queryRegistration(deptId, scheudlingType, subSectionType, regStatus, userId);
        return AjaxResult.success(list);
    }

    /**
     * 查询就诊完成的挂号信息
     * doctor/care/queryVisitCompletedRegistration/{scheudlingType}
     */
    @GetMapping("/queryVisitCompletedRegistration/{scheudlingType}")
    @HystrixCommand
    public AjaxResult queryVisitCompletedRegistration(@PathVariable String scheudlingType) {
        Long deptId = ShiroSecurityUtils.getCurrentUser().getDeptId();
        String regStatus = Constants.REG_STATUS_3;
        String subSectionType = null;
        Long userId = ShiroSecurityUtils.getCurrentUser().getUserId();
        List<Registration> list = registrationService.queryRegistration(deptId, scheudlingType, subSectionType, regStatus, userId);
        return AjaxResult.success(list);
    }

    /**
     * 接诊
     * POST/doctor/care/receivePatient/{regId}
     */
    @PostMapping("/receivePatient/{regId}")
    public AjaxResult receivePatient(@PathVariable String regId) {
        //挂号单状态转变,防止多个医生接诊同一个患者，所以加了锁
        synchronized (this) {
            Registration registration = registrationService.queryRegistrationById(regId);
            if (registration == null) {
                return AjaxResult.fail("【" + regId + "】挂号单的不存在，不能接诊");
            }
            if (registration.getRegStatus().equals(Constants.REG_STATUS_1)) {
                registration.setRegStatus(Constants.REG_STATUS_2);
                registration.setUserId(ShiroSecurityUtils.getCurrentUser().getUserId());
                registration.setDoctorName(ShiroSecurityUtils.getCurrentUserName());
                return AjaxResult.toAjax(registrationService.updateRegistrationById(registration));
            } else {
                return AjaxResult.fail("挂号单【" + regId + "】的状态不是待就诊状态，无法接诊");
            }
        }

    }

    /**
     * 根据患者ID获取患者信息、档案信息、病历信息
     * GET/doctor/care/getPatientAllMessageByPatientId/{patientId}
     */
    @GetMapping("/getPatientAllMessageByPatientId/{patientId}")
    public AjaxResult getPatientAllMessageByPatientId(@PathVariable String patientId) {
        //患者信息
        Patient patient = patientService.getPatientById(patientId);
        //病例信息
        List<CareHistory> careHistory = careService.getCareHistoryByPatientId(patientId);
        //档案信息
        PatientFile patienFile = patientService.getPatienFiletById(patientId);
        //map组装
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("patient", patient);
        map.put("patientFile", patienFile);
        map.put("careHistoryList", careHistory);
        return AjaxResult.success(map);
    }

    /**
     * 保存病例
     *
     * @param careHistoryDto
     * @return
     */
    @PostMapping("/saveCareHistory")
    public AjaxResult saveCareHistory(@RequestBody CareHistoryDto careHistoryDto) {
        careHistoryDto.setUserId(ShiroSecurityUtils.getCurrentUser().getUserId());
        careHistoryDto.setUserName(ShiroSecurityUtils.getCurrentUserName());
        careHistoryDto.setDeptId(ShiroSecurityUtils.getCurrentUser().getDeptId());
        Dept dept = deptService.getOne(ShiroSecurityUtils.getCurrentUser().getDeptId());
        careHistoryDto.setDeptName(dept.getDeptName());
        careHistoryDto.setCareDate(DateUtil.date());
        CareHistory careHistory = careService.saveOrUpdateCareHistory(careHistoryDto);
        return AjaxResult.success(careHistory);
    }

    /**
     * 根据挂号id查询病例
     *
     * @param regId
     * @return
     */
    @GetMapping("/getCareHistoryByRegId/{regId}")
    public AjaxResult getCareHistoryByRegId(@PathVariable String regId) {
        return AjaxResult.success(careService.queryCareHistoryByRegId(regId));
    }

    @GetMapping("/queryCareOrdersByChId/{chId}")
    public AjaxResult queryCareOrdersByChId(@PathVariable String chId) {
        List<CareOrder> careOrders = careService.queryCareOrdersByChId(chId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (CareOrder careOrder : careOrders) {
            Map<String, Object> map = new HashMap<>();
            map.put("careOrder", careOrder);
            List<CareOrderItem> careOrderItems = careService.queryCareOrderItemsByCoId(careOrder.getCoId());
            map.put("careOrderItems", careOrderItems);
            list.add(map);
        }
        return AjaxResult.success(list);
    }

    @PostMapping("/saveCareOrderItem")
    @HystrixCommand
    public AjaxResult saveCareOrderItem(@RequestBody @Valid CareOrderFormDto careOrderFormDto) {
        CareHistory careHistory = careService.queryCareHistoryByChId(careOrderFormDto.getCareOrder().getChId());
        if (careHistory == null) {
            return AjaxResult.fail("病例不存在，请核对后再提交");
        }
        careOrderFormDto.getCareOrder().setCoId(IdGeneratorSnowflake.generatorIdWithProfix(Constants.ID_PROFIX_CO));
        careOrderFormDto.getCareOrder().setUserId(ShiroSecurityUtils.getCurrentUser().getUserId());
        careOrderFormDto.getCareOrder().setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(careService.saveCareOrderItem(careOrderFormDto));
    }

    /**
     * 根据处方详情 ID 删除处方详情【只能删除未支付的】
     */
    @DeleteMapping("deleteCareOrderItemById/{itemId}")
    @HystrixCommand
    public AjaxResult deleteCareOrderItemById(@PathVariable String itemId) {
        CareOrderItem careOrderItem = this.careService.queryCareOrderItemByItemId(itemId);
        if (null == careOrderItem) {
            return AjaxResult.fail("处方详情 ID 不存在");
        }
        if (!careOrderItem.getStatus().equals(Constants.ORDER_BACKFEE_STATUS_0)) {
            return AjaxResult.fail("【" + itemId + "】不是未支付状态，不能删除");
        }
        return AjaxResult.toAjax(careService.deleteCareOrderItemByItemId(itemId));
    }

    /**
     * 完成就诊
     */
    @PostMapping("visitComplete/{regId}")
    @HystrixCommand
    public AjaxResult visitComplete(@PathVariable String regId) {
        Registration registration = this.registrationService.queryRegistrationById(regId);
        if (null == registration) {
            return AjaxResult.fail("【" + regId + "】挂号单号不存在，请核对后再提交");
        }
        if (!registration.getRegStatus().equals(Constants.REG_STATUS_2)) {
            return AjaxResult.fail("【" + regId + "】状态不是就诊中状态，不能完成就诊");
        }
        //更改挂号单的状态
        return AjaxResult.toAjax(this.careService.visitComplete(regId));
    }

}
