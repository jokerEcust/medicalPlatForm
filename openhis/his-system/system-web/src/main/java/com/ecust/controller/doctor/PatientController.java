package com.ecust.controller.doctor;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.ecust.controller.BaseController;
import com.ecust.domain.CareHistory;
import com.ecust.domain.CareOrder;
import com.ecust.domain.CareOrderItem;
import com.ecust.dto.PatientDto;
import com.ecust.service.CareService;
import com.ecust.service.PatientService;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/doctor/patient")
public class PatientController extends BaseController {
    @Reference
    PatientService patientService;

    @Reference
    CareService careService;

    /**
     * 分页查询患者库
     *
     * @return
     */
    @GetMapping("/listPatientForPage")
    @HystrixCommand
    public AjaxResult listPatientForPage(PatientDto patientDto) {
        DataGridView dataGridView = patientService.listPagePatient(patientDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    /**
     * 根据id查询患者信息
     *
     * @param patientId
     * @return
     */
    @GetMapping("/getPatientById/{patientId}")
    @HystrixCommand
    public AjaxResult getPatientById(@PathVariable String patientId) {
        return AjaxResult.success(patientService.queryPatientById(patientId));
    }

    /**
     * 根据患者身份id查询患者档案信息
     *
     * @param patientId
     * @return
     */
    @GetMapping("/getPatientFileById/{patientId}")
    @HystrixCommand
    public AjaxResult getPatientFileById(@PathVariable String patientId) {
        return AjaxResult.success(patientService.queryPatientFileById(patientId));
    }

    /**
     * 根据患者id查询患者所有信息
     *
     * @param patientId
     * @return
     */
    @GetMapping("/getPatientAllMessageByPatientId/{patientId}")
    @HystrixCommand
    public AjaxResult getPatientAllMessageByPatientId(@PathVariable String patientId) {
        List<CareHistory> careHistories = careService.getCareHistoryByPatientId(patientId);
        List<Map<String, Object>> list = new ArrayList<>();
        for (CareHistory careHistory : careHistories) {
            Map<String, Object> careHistoryMap = BeanUtil.beanToMap(careHistory);
            //添加一个键值对，即处方
            String chId = careHistory.getChId();
            List<CareOrder> careOrders = careService.queryCareOrdersByChId(chId);
            List<Map<String, Object>> list1 = new ArrayList<>();
            for (CareOrder careOrder : careOrders) {
                Map<String, Object> careOrdersMap = BeanUtil.beanToMap(careOrder);
                String coId = careOrder.getCoId();
                List<CareOrderItem> careOrderItems = careService.queryCareOrderItemsByCoId(coId);
                List<Map<String, Object>> list3 = new ArrayList<>();
                for (CareOrderItem careOrderItem : careOrderItems) {
                    Map<String, Object> careOrderItemsMap = BeanUtil.beanToMap(careOrderItem);
                    list3.add(careOrderItemsMap);
                }
                careOrdersMap.put("careOrderItems", list3);
                list1.add(careOrdersMap);
            }
            careHistoryMap.put("careOrders", list1);
            list.add(careHistoryMap);
        }
        return AjaxResult.success(list);


    }
}
