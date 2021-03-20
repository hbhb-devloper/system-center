package com.hbhb.cw.systemcenter.rpc;

import com.hbhb.cw.publicitymanage.api.MaterialsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wangxiaogang
 */
@FeignClient(value = "${provider.publicity-manage}", url = "${feign-url}", contextId = "PublicityMaterialsApiExp", path = "materials/notice/")
public interface PublicityMaterialsApiExp extends MaterialsApi {
}
