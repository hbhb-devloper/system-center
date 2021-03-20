package com.hbhb.cw.systemcenter.rpc;

import com.hbhb.cw.fundcenter.api.NoticeApi;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${provider.fund-center}", url = "${feign-url}", contextId = "FundNoticeApi", path = "/notice")
public interface FundApiExp extends NoticeApi {
}
