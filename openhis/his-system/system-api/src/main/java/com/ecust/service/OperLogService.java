package com.ecust.service;

import com.ecust.domain.OperLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.dto.OperLogDto;
import com.ecust.vo.DataGridView;

public interface OperLogService {
    //分页查询

    /**
     * 分页查询操作日志
     *
     * @param operLogDto 操作日志对象（传输）
     * @return
     */
    DataGridView listForPage(OperLogDto operLogDto);
    //删除日志(多个)

    /**
     * 删除日志（可多个）
     *
     * @param infoIds 日志id
     * @return 1：成功；0：失败
     */
    int deleteOperLogByIds(Long[] infoIds);
    //清空日志

    /**
     * 清空日志
     *
     * @return 1：成功；0：失败
     */
    int clearAllOperLog();

    /**
     * 插入日志信息
     * @param operLog
     */
    void insertOperLog(OperLog operLog);



}
