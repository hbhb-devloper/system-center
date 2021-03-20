package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.cw.systemcenter.enums.code.UserErrorCode;
import com.hbhb.cw.systemcenter.exception.UserException;
import com.hbhb.cw.systemcenter.service.SysMaintainService;
import com.hbhb.cw.systemcenter.service.SysRoleService;
import com.hbhb.cw.systemcenter.web.vo.MaintainVO;
import com.hbhb.web.annotation.UserId;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "维护信息")
@RestController
@RequestMapping("/maintain")
public class SysMaintainController {

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysMaintainService sysMaintainService;

    @Operation(summary = "系统维护信息详情")
    @GetMapping("/info")
    public MaintainVO getMaintain() {
        return sysMaintainService.getMaintainInfo();
    }

    @Operation(summary = "修改系统维护信息")
    @PutMapping("")
    public void updateInfo(@Parameter(hidden = true) @UserId Integer userId,
                           @Parameter(description = "维护信息实体") @RequestBody MaintainVO vo) {
        boolean admin = sysRoleService.isAdminRole(userId);
        if (!admin) {
            throw new UserException(UserErrorCode.AUTHOR_NOT_ADMINISTRATOR);
        }
        sysMaintainService.updateMaintain(vo, userId);
    }
}
