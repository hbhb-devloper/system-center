package com.hbhb.cw.systemcenter.web;

import com.hbhb.cw.systemcenter.api.MailApi;
import com.hbhb.cw.systemcenter.service.MailService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author xiaokang
 * @since 2020-09-28
 */
@Tag(name = "发送邮件")
@RestController
@RequestMapping("/mail")
public class MailController implements MailApi {

    @Resource
    private MailService mailService;

    @Operation(summary = "发送邮件")
    @Override
    public void postMail(
            @Parameter(description = "接收人", required = true) @RequestParam String receiver,
            @Parameter(description = "标题", required = true) @RequestParam String title,
            @Parameter(description = "内容", required = true) @RequestParam String content) {
        mailService.sendSimpleMailMessage(receiver, title, content);
    }
}
