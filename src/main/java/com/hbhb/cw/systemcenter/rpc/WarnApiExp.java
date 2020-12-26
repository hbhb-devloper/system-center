package com.hbhb.cw.systemcenter.rpc;

import com.hbhb.cw.relocation.api.RelocationWarnApi;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author xiaokang
 * @since 2020-12-26
 */
@FeignClient(value = "${provider.relocation-manage}", url = "${feign-url}", contextId = "RelocationWarnApi", path = "/warn")
public interface WarnApiExp extends RelocationWarnApi {
}
