package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.core.bean.BeanConverter;
import com.hbhb.cw.systemcenter.model.Broadcast;
import com.hbhb.cw.systemcenter.service.BroadcastService;
import com.hbhb.cw.systemcenter.web.vo.BroadcastVO;
import com.hbhb.web.annotation.UserId;

import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author xiaokang
 * @since 2020-09-09
 */
@Tag(name = "公告管理")
@RestController
@RequestMapping("/broadcast")
public class BroadcastController {

    @Resource
    private BroadcastService broadcastService;

    @Operation(summary = "分页获取公告列表")
    @GetMapping("/list")
    public PageResult<Broadcast> getBroadcastListByPage(
            @Parameter(description = "页码，默认为1") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页数量，默认为10") @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "公告内容（模糊查询）") @RequestParam(required = false) String content,
            @Parameter(description = "公告状态（0-停用、1-启用）") @RequestParam(required = false) Byte state) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return broadcastService.pageBroadcast(pageNum, pageSize, content, state);
    }

    @Operation(summary = "获取已发布的公告列表")
    @GetMapping("/publish")
    public List<String> getBroadcastList() {
        return broadcastService.getPublishList();
    }

    @Operation(summary = "新增公告")
    @PostMapping("")
    public void addBroadcast(@Parameter(hidden = true) @UserId Integer userId,
                             @Parameter(description = "公告实体") @RequestBody BroadcastVO vo) {
        Broadcast broadcast = new Broadcast();
        BeanConverter.copyProp(vo, broadcast);
        broadcast.setCreateBy(userId);
        broadcast.setCreateTime(new Date());
        broadcastService.upsertBroadcast(broadcast);
    }

    @Operation(summary = "修改公告")
    @PutMapping("")
    public void updateBroadcast(@Parameter(description = "公告实体") @RequestBody BroadcastVO vo) {
        Broadcast broadcast = new Broadcast();
        BeanConverter.copyProp(vo, broadcast);
        broadcastService.upsertBroadcast(broadcast);
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/{broadcastId}")
    public void deleteBroadcast(@Parameter(description = "公告id") @PathVariable Long broadcastId) {
        broadcastService.deleteBroadcast(broadcastId);
    }
}
