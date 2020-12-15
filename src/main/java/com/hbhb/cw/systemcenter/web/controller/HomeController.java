package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.cw.systemcenter.service.HomeService;
import com.hbhb.cw.systemcenter.web.vo.HomeModuleVO;
import com.hbhb.cw.systemcenter.web.vo.HomeTodoVO;
import com.hbhb.web.annotation.UserId;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wxg
 * @since 2020-09-30
 */
@Tag(name = "首页", description = "新版本 /work -> /home")
@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    private HomeService homeService;

    @Operation(summary = "首页工作台模块统计")
    @GetMapping("/module")
    public List<HomeModuleVO> getModuleList(@Parameter(hidden = true) @UserId Integer userId) {
        return homeService.getModuleList(userId);
    }

    @Operation(summary = "待办事宜列表", description = "新版本 /list/{module} -> /todo/{module}")
    @GetMapping("/todo/{module}")
    public List<HomeTodoVO> getTodoList(
            @Parameter(description = "模块值") @PathVariable("module") Integer module,
            @Parameter(hidden = true) @UserId Integer userId) {
        return homeService.getTodoList(module, userId);
    }
}
