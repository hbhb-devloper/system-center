package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.core.bean.BeanConverter;
import com.hbhb.cw.systemcenter.api.HallApi;
import com.hbhb.cw.systemcenter.model.Hall;
import com.hbhb.cw.systemcenter.service.HallService;
import com.hbhb.cw.systemcenter.vo.HallReqVO;
import com.hbhb.cw.systemcenter.vo.HallResVO;
import com.hbhb.cw.systemcenter.vo.HallSelectReqVO;
import com.hbhb.web.annotation.UserId;

import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "营业厅")
@RestController
@RequestMapping("/hall")
@Slf4j
public class HallController  implements HallApi {

    @Resource
    private HallService hallService;

    @Operation(summary = "分页获取营业厅列表")
    @GetMapping("/list")
    public PageResult<HallResVO> pageHall(
            @Parameter(description = "页码，默认为1") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页数量，默认为10") @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "分公司id") @RequestParam(required = false) Integer unitId,
            @Parameter(description = "营业厅名称") @RequestParam(required = false) String hallName,
            @Parameter(description = "启用状态") @RequestParam(required = false) Boolean enable) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return hallService.pageHall(pageNum, pageSize, unitId, hallName, enable);
    }

    @Operation(summary = "获取营业厅列表", description = "下拉框用")
    @GetMapping("/select")
    public List<SelectVO> listHall(
            @Parameter(description = "分公司id", required = true) @RequestParam Integer unitId,@Parameter(hidden = true) @UserId Integer userId) {
        return hallService.listHall(userId,unitId);
    }

    @Operation(summary = "获取营业厅列表-全部列表和当前用户已选择的列表", description = "下拉框用")
    @GetMapping("/select_new")
    public Map<String, Object> listHallNew(@Parameter(description = "分公司id", required = true) @RequestParam Integer unitId,
                                           @Parameter(description = "用户id", required = true) @RequestParam Integer userId) {
        return hallService.listHallNew(userId,unitId);
    }

    @Operation(summary = "获取营业厅列表-根据用户id", description = "下拉框用")
    @Override
    public Map<Integer,String> selectHallByUnitId(@Parameter(description = "单位id") @RequestParam Integer unitId) {
        return hallService.selectHallByUnitId(unitId);
    }

    @Operation(summary = "添加营业厅")
    @PostMapping("")
    public void addHall(@Parameter(description = "营业厅信息", required = true) @RequestBody HallReqVO vo,
            @Parameter(hidden = true) @UserId Integer userId) {
        Hall hall = BeanConverter.convert(vo, Hall.class);
        hall.setCreateBy(userId.toString());
        hall.setCreateTime(new Date());
        hallService.upsertHall(hall);
    }

    @Operation(summary = "更新营业厅")
    @PutMapping("")
    public void updateHall(@Parameter(description = "营业厅信息", required = true) @RequestBody HallReqVO vo) {
        hallService.upsertHall(BeanConverter.convert(vo, Hall.class));
    }

    @Operation(summary = "更新营业厅-选择营业厅")
    @PutMapping("/updateHallNew")
    public void updateHallNew(@Parameter(description = "营业厅信息", required = true) @RequestBody HallSelectReqVO vo,
                              @Parameter(description = "用户id", required = true) @RequestParam Integer userId) {
        hallService.updateHallNew(userId,vo.getUnitId(),vo.getHallSelectIds());
    }


}
