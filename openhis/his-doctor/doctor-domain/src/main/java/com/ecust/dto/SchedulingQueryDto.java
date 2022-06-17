package com.ecust.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 页面传过来的数据结构
 */
@ApiModel(value="com-ecust-dto-SchedulingQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingQueryDto implements Serializable {
    private Long deptId;//部门id
    private Long userId;//用户id
    private String queryDate;//页面传来的日期

    private String beginDate;
    private String endDate;

}
