package com.hbhb.cw.systemcenter.rpc;


import com.hbhb.cw.publicitymanage.api.PictureNoticeApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author wangxiaogang
 */
@FeignClient(value = "${provider.publicity-manage}", url = "${feign-url}", contextId = "PublicityPictureApiExp", path = "picture/notice/")
public interface PublicityPictureApiExp extends PictureNoticeApi {
}
