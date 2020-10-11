package com.hbhb.cw.systemcenter.web;

import com.hbhb.cw.systemcenter.service.MailService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author xiaokang
 * @since 2020-09-28
 */
@Api(tags = "发送邮件")
@RestController
@RequestMapping("/mail")
public class MailController {

    @Resource
    private MailService mailService;

    @ApiOperation("发送邮件")
    @PostMapping("/send")
    public void sendMail(
            @ApiParam(value = "接收人", required = true) @RequestParam String receiver,
            @ApiParam(value = "标题", required = true) @RequestParam String title,
            @ApiParam(value = "内容", required = true) @RequestParam String content) {
        mailService.sendSimpleMailMessage(receiver, title, content);
    }
}
