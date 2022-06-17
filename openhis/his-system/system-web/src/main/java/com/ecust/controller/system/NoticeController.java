package com.ecust.controller.system;

import com.ecust.dto.NoticeDto;
import com.ecust.service.NoticeService;
import com.ecust.utils.ShiroSecurityUtils;
import com.ecust.vo.AjaxResult;
import com.ecust.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/system/notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    @GetMapping("/listNoticeForPage")
    public AjaxResult listNoticeForPage(NoticeDto noticeDto) {
        DataGridView dataGridView = noticeService.listForPage(noticeDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    @PostMapping("/addNotice")
    public AjaxResult addNotice(@Valid NoticeDto noticeDto) {
        noticeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(noticeService.addNotice(noticeDto));
    }

    @GetMapping("/getNoticeById/{noticeId}")
    public AjaxResult getNoticeById(@PathVariable Long noticeId) {
        return AjaxResult.success(noticeService.queryNoticeById(noticeId));
    }

    @PutMapping("/updateNotice")
    public AjaxResult updateNotice(@Valid NoticeDto noticeDto) {
        noticeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(noticeService.updateNotice(noticeDto));
    }

    @DeleteMapping("/deleteNoticeByIds/{noticeIds}")
    public AjaxResult deleteNoticeByIds(@PathVariable Long[] noticeIds) {
        return AjaxResult.toAjax(noticeService.deleteNoticeByIds(noticeIds));

    }

}
