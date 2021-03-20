package com.hbhb.cw.systemcenter.rpc;

import com.hbhb.cw.fundcenter.api.NoticeApi;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${provider.budget-service}", url = "${feign-url}", contextId = "BudgetNoticeApi", path = "/notice")
public interface BudgetApiExp extends NoticeApi {
}
