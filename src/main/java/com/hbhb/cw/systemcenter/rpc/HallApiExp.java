package com.hbhb.cw.systemcenter.rpc;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "${provider.fund-center}", url = "${feign-url}", contextId = "HallApiExp", path = "/hall")
public interface HallApiExp {


}
