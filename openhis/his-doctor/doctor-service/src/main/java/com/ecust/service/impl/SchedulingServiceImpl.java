package com.ecust.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecust.domain.Scheduling;
import com.ecust.domain.SimpleUser;
import com.ecust.dto.RegistrationQueryDto;
import com.ecust.dto.SchedulingFormQueryDto;
import com.ecust.dto.SchedulingQueryDto;
import com.ecust.mapper.SchedulingMapper;
import com.ecust.service.SchedulingService;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class SchedulingServiceImpl implements SchedulingService {
    @Autowired
    SchedulingMapper schedulingMapper;

    @Override
    public List<Scheduling> queryScheduling(SchedulingQueryDto schedulingQueryDto) {
        QueryWrapper<Scheduling> qw = new QueryWrapper<>();
        qw.eq(schedulingQueryDto.getUserId() != null, Scheduling.COL_USER_ID, schedulingQueryDto.getUserId());
        qw.eq(schedulingQueryDto.getDeptId() != null, Scheduling.COL_DEPT_ID, schedulingQueryDto.getDeptId());
        qw.ge(Scheduling.COL_SCHEDULING_DAY, schedulingQueryDto.getBeginDate());
        qw.le(Scheduling.COL_SCHEDULING_DAY, schedulingQueryDto.getEndDate());
        return schedulingMapper.selectList(qw);
    }

    @Override
    public int saveScheduling(SchedulingFormQueryDto schedulingFormQueryDto, SimpleUser simpleUser) {
        DateTime dateTime = DateUtil.parse(schedulingFormQueryDto.getBeginDate(), "yyyy-MM-dd");
        DateTime date = DateUtil.beginOfWeek(dateTime);
        String beginDate = DateUtil.format(date, "yyyy-MM-dd");//获取本周周一
        String endDate = DateUtil.format(DateUtil.endOfWeek(dateTime), "yyyy-MM-dd");//获取本周周末
        List<SchedulingFormQueryDto.SchedulingData> data = schedulingFormQueryDto.getData();
        //先删除掉以前的排班信息
        Long userId = data.get(0).getUserId();
        Long deptId = data.get(0).getDeptId();
        if (userId!=null){
            //清除一周内该医生的全部排班记录
            QueryWrapper<Scheduling> qw=new QueryWrapper<>();
            qw.eq(Scheduling.COL_USER_ID,userId);
            qw.eq(Scheduling.COL_DEPT_ID,deptId);
            qw.between(Scheduling.COL_SCHEDULING_DAY,beginDate,endDate);
            schedulingMapper.delete(qw);
            List<String> schedulingDays = initSchedulingDay(date);

            for (SchedulingFormQueryDto.SchedulingData schedulingData : data) {
                String subsectionType=schedulingData.getSubsectionType();
                int i=-1;
                for (String s : schedulingData.getSchedulingType()) {
                    i++;
                    if (StringUtils.isNoneBlank(s)){
                        Scheduling scheduling=new Scheduling(userId,deptId,schedulingDays.get(i),subsectionType,s,DateUtil.date(),simpleUser.getUserName());
                        schedulingMapper.insert(scheduling);
                    }
                }

            }
            return 1;
        }else {
            //医生不能存在直接返回0
            return 0;
        }

    }

    @Override
    public List<Long> queryHasSchedulingDeptIds(RegistrationQueryDto registrationQueryDto) {
        QueryWrapper<Scheduling> qw=new QueryWrapper<>();
        qw.eq(Scheduling.COL_DEPT_ID,registrationQueryDto.getDeptId());
        qw.eq(Scheduling.COL_SCHEDULING_DAY,registrationQueryDto.getSchedulingDay());
        qw.eq(Scheduling.COL_SUBSECTION_TYPE,registrationQueryDto.getSubsectionType());
        qw.eq(Scheduling.COL_SCHEDULING_TYPE,registrationQueryDto.getSchedulingType());
        List<Scheduling> schedulings = schedulingMapper.selectList(qw);
        List<Long> deptIds=new ArrayList<>();
        for (Scheduling scheduling : schedulings) {
            deptIds.add(scheduling.getDeptId());
        }
        return deptIds;
    }

    private List<String> initSchedulingDay(Date startDate) {
        List<String> list = new ArrayList<>();
        for (int i=0;i<7;i++){
            DateTime d = DateUtil.offsetDay(startDate,i);
            String key = DateUtil.format(d, "yyyy-MM-dd");
            list.add(key);
        }
        return list;

    }


}
