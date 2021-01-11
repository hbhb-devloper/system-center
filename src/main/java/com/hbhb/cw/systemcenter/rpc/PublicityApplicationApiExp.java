package com.hbhb.cw.systemcenter.rpc;


import com.hbhb.cw.publicity.api.PublicityApplicationApi;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author yzc
 * @since 2021-01-06
 */
@FeignClient(value = "${provider.publicity-manage}", url = "${feign-url}", contextId = "PublicityApplicationApiExp", path = "application/notice/")
public interface PublicityApplicationApiExp extends PublicityApplicationApi {
}
