package com.hbhb.cw.systemcenter.rpc;


import com.hbhb.cw.publicity.api.PublicityVerifyApi;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author yzc
 * @since 2021-01-08
 */
@FeignClient(value = "${provider.publicity-manage}", url = "${feign-url}", contextId = "PublicityVerifyApExp", path = "verify/notice/")
public interface PublicityVerifyApiExp extends PublicityVerifyApi {
}
