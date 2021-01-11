package com.hbhb.cw.systemcenter.rpc;

import com.hbhb.cw.publicitymanage.api.PrintNoticeApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wangxiaogang
 */
@FeignClient(value = "${provider.publicity-manage}", url = "${feign-url}", contextId = "PublicityPrintApiExp", path = "print/notice/")
public interface PublicityPrintApiExp extends PrintNoticeApi {
}
