package com.hbhb.cw.systemcenter.rpc;

import com.hbhb.cw.reportmanage.api.reportApi;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author yzc
 * @since 2021-01-21
 */
@FeignClient(value = "${provider.report-manage}", url = "${feign-url}", contextId = "ReportApExp", path = "report/notice/")
public interface ReportApiExp extends reportApi {
}
