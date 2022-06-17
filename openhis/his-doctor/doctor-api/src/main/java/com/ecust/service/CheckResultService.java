package com.ecust.service;

import com.ecust.domain.CheckResult;
import com.ecust.dto.CheckResultDto;
import com.ecust.dto.CheckResultFormDto;
import com.ecust.vo.DataGridView;

public interface CheckResultService {
    /**
     * 保存检查项目信息
     *
     * @param checkResult
     * @return
     */
    int saveCheckResult(CheckResult checkResult);

    DataGridView queryAllCheckResultForPage(CheckResultDto checkResultDto);

    int completeCheckResult(CheckResultFormDto checkResultFormDto);
}
