package com.ecust.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecust.dto.NoticeDto;
import com.ecust.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecust.mapper.NoticeMapper;
import com.ecust.domain.Notice;
import com.ecust.service.NoticeService;
@Service
public class NoticeServiceImpl implements NoticeService{
    @Autowired
    NoticeMapper noticeMapper;

    /**
     * 分页查询
     * @param noticeDto
     * @return
     */
    @Override
    public DataGridView listForPage(NoticeDto noticeDto) {
        QueryWrapper<Notice> queryWrapper=new QueryWrapper<>();
        Page<Notice> page=new Page<>(noticeDto.getPageNum(),noticeDto.getPageSize());
        queryWrapper.like(StringUtils.isNoneBlank(noticeDto.getNoticeTitle()),Notice.COL_NOTICE_TITLE,noticeDto.getNoticeTitle());
        queryWrapper.eq(StringUtils.isNotBlank(noticeDto.getStatus()),Notice.COL_STATUS,noticeDto.getStatus());
        queryWrapper.eq(StringUtils.isNotBlank(noticeDto.getNoticeType()),Notice.COL_NOTICE_TYPE,noticeDto.getNoticeType());
        queryWrapper.like(StringUtils.isNoneBlank(noticeDto.getCreateBy()),Notice.COL_CREATE_BY,noticeDto.getCreateBy());
        queryWrapper.orderByDesc(Notice.COL_CREATE_TIME);
//        queryWrapper.ge(noticeDto.getBeginTime()!=null,Notice.COL_CREATE_TIME,noticeDto.getBeginTime());
//        queryWrapper.le(noticeDto.getEndTime()!=null,Notice.COL_CREATE_TIME,noticeDto.getEndTime());
        noticeMapper.selectPage(page,queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addNotice(NoticeDto noticeDto) {
        Notice notice = new Notice();
        BeanUtil.copyProperties(noticeDto,notice);
//        notice.setCreateBy(noticeDto.getSimpleUser().getUserName());
        notice.setCreateTime(DateUtil.date());
        return noticeMapper.insert(notice);
    }

    @Override
    public Notice queryNoticeById(Long noticeId) {
        if (noticeId==null){
            return null;
        }
        return noticeMapper.selectById(noticeId);
    }

    @Override
    public int updateNotice(NoticeDto noticeDto) {
        Notice notice = new Notice();
        BeanUtil.copyProperties(noticeDto,notice);
        notice.setUpdateBy(noticeDto.getSimpleUser().getUserName());
        return noticeMapper.updateById(notice);
    }

    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        if (noticeIds==null||noticeIds.length==0){
            return 0;
        }
        List<Long> ids = Arrays.asList(noticeIds);
        return noticeMapper.deleteBatchIds(ids);

    }
}
