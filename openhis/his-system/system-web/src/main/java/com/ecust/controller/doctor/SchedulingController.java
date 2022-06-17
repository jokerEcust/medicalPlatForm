package com.ecust.controller.doctor;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ecust.controller.BaseController;
import com.ecust.domain.Scheduling;
import com.ecust.domain.SimpleUser;
import com.ecust.domain.User;
import com.ecust.dto.SchedulingDto;
import com.ecust.dto.SchedulingFormQueryDto;
import com.ecust.dto.SchedulingQueryDto;
import com.ecust.service.SchedulingService;
import com.ecust.service.UserService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/doctor/scheduling")
@RestController
public class SchedulingController extends BaseController {
    @Reference
    SchedulingService schedulingService;

    @Autowired
    UserService userService;

    //考虑到有查询医生排班和科室排班的情况，这里设计同一个服务方法，传入两个参数----userId、deptId
    //不是分页查询，不需要构造datagridview
    @GetMapping("/queryUsersNeedScheduling")
    public AjaxResult queryUsersNeedScheduling(Long deptId) {
        List<User> list = userService.queryUsersNeedScheduling(null, deptId);
        return AjaxResult.success(list);
    }

    @GetMapping({"/queryScheduling"})
    public AjaxResult queryScheduling(SchedulingQueryDto schedulingQueryDto) {
        List<User> lists = userService.queryUsersNeedScheduling(schedulingQueryDto.getUserId(), schedulingQueryDto.getDeptId());
        //lists存放医生们在某个科室排版所有的信息
        Date date = DateUtil.date();
        if (StringUtils.isNoneBlank(schedulingQueryDto.getQueryDate())) {
            //如果传来的日期不为空
            date = DateUtil.parse(schedulingQueryDto.getQueryDate(), "yyyy-MM-dd");
            int i = DateUtil.dayOfWeek(date);//判断周几(1--周日，2--周一....)
            //如果为一，则查询的是下周的信息，为2查询的是上周的信息
            if (i == 1) {
                date = DateUtil.offsetDay(date, 1);
            } else {
                date = DateUtil.offsetDay(date, -1);
            }
        }
        //计算当前日期所在周的开始与结束日期
        DateTime beginTime = DateUtil.beginOfWeek(date);
        DateTime endTime = DateUtil.endOfWeek(date);
        schedulingQueryDto.setBeginDate(DateUtil.format(beginTime, "yyyy-MM-dd"));
        schedulingQueryDto.setEndDate(DateUtil.format(endTime, "yyyy-MM-dd"));
        List<Scheduling> ulist = schedulingService.queryScheduling(schedulingQueryDto);
        //开始构造SchedulingDto的数据结构
        List<SchedulingDto> schedulingDtos = new ArrayList<>();
        for (User user : lists) {
            //对于单个医生，最多有三条数据，结构为：{userId,deptId,subType(上午),schType：["","","","","","",""],...}
            SchedulingDto schedulingDto1 = new SchedulingDto(user.getUserId(), user.getDeptId(), "1", initMap(beginTime));
            SchedulingDto schedulingDto2 = new SchedulingDto(user.getUserId(), user.getDeptId(), "2", initMap(beginTime));
            SchedulingDto schedulingDto3 = new SchedulingDto(user.getUserId(), user.getDeptId(), "3", initMap(beginTime));
            schedulingDtos.add(schedulingDto1);
            schedulingDtos.add(schedulingDto2);
            schedulingDtos.add(schedulingDto3);
            for (Scheduling scheduling : ulist) {
                Long userId = scheduling.getUserId();
                String subsectionType = scheduling.getSubsectionType();
                String schedulingDay = scheduling.getSchedulingDay();
                if (user.getUserId().equals(userId)) {
                    switch (subsectionType) {
                        case "1":
                            Map<String, String> record1 = schedulingDto1.getRecord();
                            record1.put(schedulingDay, scheduling.getSchedulingType());
                            break;
                        case "2":
                            Map<String, String> record2 = schedulingDto2.getRecord();
                            record2.put(schedulingDay, scheduling.getSchedulingType());
                            break;
                        case "3":
                            Map<String, String> record3 = schedulingDto3.getRecord();
                            record3.put(schedulingDay, scheduling.getSchedulingType());
                            break;
                    }
                }
            }
            schedulingDto1.setSchedulingType(schedulingDto1.getRecord().values());
            schedulingDto2.setSchedulingType(schedulingDto2.getRecord().values());
            schedulingDto3.setSchedulingType(schedulingDto3.getRecord().values());

        }
        Map<String, Object> res = new HashMap<>();
        //封装labelnresames与scheduledata
        Map<String, Object> schedulingData = new HashMap<>();
        schedulingData.put("endTimeThisWeek", schedulingQueryDto.getEndDate());
        schedulingData.put("startTimeThisWeek", schedulingQueryDto.getBeginDate());
        res.put("schedulingData", schedulingData);
        res.put("tableData", schedulingDtos);
        res.put("labelNames", getLabelNames(beginTime));
        return AjaxResult.success(res);
    }

    private Map<String, String> initMap(DateTime beginTime) {
        Map<String, String> map = new TreeMap<>();//按顺序放 2020-08-03 - 2020-08-09
        for (int i = 0; i < 7; i++) {
            DateTime d = DateUtil.offsetDay(beginTime, i);
            String key = DateUtil.format(d, "yyyy-MM-dd");
            map.put(key, "");
        }
        return map;
    }

    private String[] getLabelNames(DateTime beginTime) {
        String[] labelNames = new String[7];
        for (int i = 0; i < 7; i++) {
            DateTime d = DateUtil.offsetDay(beginTime, i);
            labelNames[i] = DateUtil.format(d, "yyyy-MM-dd") + formatterWeek(i);
        }
        return labelNames;
    }

    private String formatterWeek(int i) {
        switch (i) {
            case 0:
                return "(周一)";
            case 1:
                return "(周二)";
            case 2:
                return "(周三)";
            case 3:
                return "(周四)";
            case 4:
                return "(周五)";
            case 5:
                return "(周六)";
            default:
                return "(周日)";
        }

    }

    @PostMapping("/saveScheduling")
    public AjaxResult saveScheduling(@RequestBody SchedulingFormQueryDto schedulingFormQueryDto){
        //这里重要还是要看一下数据库需要存储哪些数据，即每个字段都需要数据来源，每个接口亦是如此
        SimpleUser simpleUser = ShiroSecurityUtils.getCurrentSimpleUser();
        int i=schedulingService.saveScheduling(schedulingFormQueryDto,simpleUser);
        return AjaxResult.toAjax(i);
    }

    @GetMapping("/queryMyScheduling")
    public AjaxResult queryMyScheduling(){
        SchedulingQueryDto queryDto = new SchedulingQueryDto();
        queryDto.setUserId((Long) ShiroSecurityUtils.getCurrentSimpleUser().getUserId());
        return queryScheduling(queryDto);
    }
}
