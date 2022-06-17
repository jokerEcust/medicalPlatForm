package com.ecust.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
    * 排班信息表
 * 排班的数据结构有些不一样，前端要求的并不是单纯的实体,需要自己设计数据实体
 *
    */
@ApiModel(value="com-ecust-dto-SchedulingDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingDto implements Serializable {
    private Long userId;
    private Long deptId;
    private String subsectionType;
    private Collection<String> schedulingType;

    @JsonIgnore
    private Map<String,String> record;
    public SchedulingDto(Long userId, Long deptId, String subsectionType, Map map) {
        this.userId = userId;
        this.deptId = deptId;
        this.subsectionType = subsectionType;
        this.record = map;
    }
}