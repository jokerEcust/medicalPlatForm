package com.ecust.service;

import com.ecust.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.dto.NoticeDto;
import com.ecust.vo.DataGridView;

public interface NoticeService{


    DataGridView listForPage(NoticeDto noticeDto);

    int addNotice(NoticeDto noticeDto);

    Notice queryNoticeById(Long noticeId);

    int updateNotice(NoticeDto noticeDto);

    int deleteNoticeByIds(Long[] noticeIds);

}
