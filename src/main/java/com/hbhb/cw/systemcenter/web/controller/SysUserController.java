package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.core.bean.BeanConverter;
import com.hbhb.cw.systemcenter.api.UserApi;
import com.hbhb.cw.systemcenter.enums.ResourceType;
import com.hbhb.cw.systemcenter.enums.RoleType;
import com.hbhb.cw.systemcenter.enums.UnitEnum;
import com.hbhb.cw.systemcenter.mapper.SysUserUintHallMapper;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.model.SysUserUintHall;
import com.hbhb.cw.systemcenter.service.SysResourceService;
import com.hbhb.cw.systemcenter.service.SysRoleService;
import com.hbhb.cw.systemcenter.service.SysUserService;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.vo.RouterVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.vo.UserInfoVO;
import com.hbhb.cw.systemcenter.vo.UserReqVO;
import com.hbhb.cw.systemcenter.vo.UserResVO;
import com.hbhb.cw.systemcenter.web.vo.UserDetailVO;
import com.hbhb.cw.systemcenter.web.vo.UserPwdVO;
import com.hbhb.web.annotation.UserId;

import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Tag(name = "用户")
@RestController
@RequestMapping("/user")
@Slf4j
public class SysUserController implements UserApi {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private UnitService unitService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysResourceService sysResourceService;
    @Resource
    private SysUserUintHallMapper sysUserUintHallMapper;

    @Operation(summary = "通过指定条件查询用户列表（分页）")
    @GetMapping("/list")
    public PageResult<UserResVO> pageUserByCond(
            @Parameter(description = "页码") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "单位id") @RequestParam(required = false) Integer unitId,
            @Parameter(description = "登录名") @RequestParam(required = false) String userName,
            @Parameter(description = "用户名") @RequestParam(required = false) String nickName,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "状态（0-禁用、1-启用）") @RequestParam(required = false) Byte state) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        unitId = unitId == null ? UnitEnum.HANGZHOU.value() : unitId;
        List<Integer> unitIds = unitService.getSubUnit(unitId);
        return sysUserService.getUserPageByCond(
                pageNum, pageSize, UserReqVO.builder()
                        .unitIds(unitIds)
                        .userName(userName)
                        .nickName(nickName)
                        .phone(phone)
                        .state(state)
                        .build());
    }

    @Operation(summary = "获取所有用户map", description = "用户id : 用户昵称-单位名称")
    @GetMapping("/select")
    public List<SelectVO> getAllUserMap() {
        return sysUserService.getAllUserMap();
    }

    @Operation(summary = "按用户id获取详细信息", description = "管理员查看用户详情页面")
    @GetMapping("/{userId}")
    public SysUser getUserInfo(
            @Parameter(description = "用户id", required = true) @PathVariable Integer userId) {
        SysUser user = sysUserService.getUserById(userId);
        List<Integer> checkedRsRoleIds = sysRoleService.getCheckedRoleByUser(userId, RoleType.RELATE_RESOURCE.value());
        List<Integer> checkedUnRoleIds = sysRoleService.getCheckedRoleByUser(userId, RoleType.RELATE_UNIT.value());
        List<Integer> checkedUintIds = sysUserUintHallMapper.createLambdaQuery().andEq(SysUserUintHall::getUserId,userId)
                .select().stream().map(SysUserUintHall::getUintId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        user.setCheckedRsRoleIds(checkedRsRoleIds);
        user.setCheckedUnRoleIds(checkedUnRoleIds);
        user.setCheckedUintIds(checkedUintIds);
        return user;
    }

    @Operation(summary = "添加用户", description = "管理员新增用户信息页面")
    @PostMapping("")
    public void addUser(@Parameter(description = "用户信息", required = true) @RequestBody SysUser user) {
        sysUserService.addUser(user);
    }

    @Operation(summary = "修改用户", description = "管理员修改用户信息页面")
    @PutMapping("")
    public void updateUser(@Parameter(description = "用户信息", required = true) @RequestBody SysUser user) {
        sysUserService.updateUser(user);
    }

    @Operation(summary = "转换用户状态")
    @PutMapping("/{userId}/state")
    public void changeUserState(
            @Parameter(description = "用户id", required = true) @PathVariable Integer userId,
            @Parameter(description = "状态值（0-禁用、1-启用）", required = true) @RequestParam Byte state) {
        sysUserService.changeUserState(userId, state);
    }

    @Operation(summary = "修改用户密码", description = "个人中心页面")
    @PutMapping("/pwd")
    public void updateUserPwd(@Parameter(required = true) @RequestBody UserPwdVO vo,
                              @Parameter(hidden = true) @UserId Integer userId) {
        sysUserService.updateUserPwd(userId, vo.getOldPwd(), vo.getNewPwd());
    }

    @Operation(summary = "个人中心-获取用户详情")
    @GetMapping("/center-info")
    public UserDetailVO getUserCenterInfo(@Parameter(hidden = true) @UserId Integer userId) {
        SysUser user = sysUserService.getUserById(userId);
        return BeanConverter.convert(user, UserDetailVO.class);
    }

    @Operation(summary = "个人中心-修改用户信息")
    @PutMapping("/center-info")
    public void updateUserCenterInfo(
            @Parameter(description = "用户信息", required = true) @RequestBody UserDetailVO vo) {
        SysUser user = new SysUser();
        BeanConverter.copyProp(vo, user);
        sysUserService.updateUserInfo(user);
    }

    @Operation(summary = "获取当前登录用户的信息（包括基本信息、资源权限等）")
    @GetMapping("/current")
    public UserInfoVO getCurrentUser(@Parameter(hidden = true) @UserId Integer userId) {
        UserInfo user = sysUserService.getUserInfoById(userId);
        // 获取登录用户的按钮权限
        Set<String> permissions = sysResourceService.getUserPermission(
                userId, Collections.singletonList(ResourceType.BUTTON.value()));
        // 获取登录用户的菜单路由
        List<RouterVO> navRouters = sysResourceService.getNavMenuTreeByUserId(userId);
        List<RouterVO> sideRouters = sysResourceService.getSideMenuTreeByUserId(userId);
        return UserInfoVO.builder()
                .userInfo(user)
                .permissions(permissions)
                .navRouters(navRouters)
                .sideRouters(sideRouters)
                .build();
    }

    @Operation(summary = "按用户id获取用户的基本信息")
    @Override
    public UserInfo getUserInfoById(@Parameter(description = "用户id", required = true) Integer userId) {
        return sysUserService.getUserInfoById(userId);
    }

    @Operation(summary = "按登录名获取用户的基本信息")
    @Override
    public UserInfo getUserInfoByName(@Parameter(description = "登录名", required = true) String userName) {
        return sysUserService.getUserInfoByName(userName);
    }

    @Operation(summary = "按id批量查询用户")
    @Override
    public List<UserInfo> getUserInfoBatch(
            @Parameter(description = "用户id列表", required = true) List<Integer> userIds) {
        return sysUserService.getUserInfoList(userIds);
    }

    @Operation(summary = "获取指定的用户map", description = "用户id-用户姓名")
    @Override
    public Map<Integer, String> getUserMapById(
            @Parameter(description = "用户id列表（为空时查询全部）") List<Integer> userIds) {
        return sysUserService.getUserMapById(userIds);
    }

    @Operation(summary = "获取用户所有角色")
    @Override
    public List<Integer> getUserRoles(@Parameter(description = "用户id", required = true) Integer userId) {
        return sysRoleService.getRolesByUserId(userId);
    }

    @Operation(summary = "校验用户是否为管理员")
    @Override
    public Boolean isAdmin(@Parameter(description = "用户id", required = true) Integer userId) {
        return sysRoleService.isAdminRole(userId);
    }

    @Operation(summary = "获取用户的资源权限（按类型）")
    @Override
    public Set<String> getUserPermission(
            @Parameter(description = "用户id", required = true) Integer userId,
            @Parameter(description = "资源类型", required = true) List<String> types) {
        return sysResourceService.getUserPermission(userId, types);
    }

    @Operation(summary = "通过单位查询用户map（userId => userName）")
    @Override
    public Map<Integer,String> getUserByUnitIds(
            @Parameter(description = "单位id") @RequestParam(required = false) Integer unitId) {
        unitId = unitId == null ? UnitEnum.HANGZHOU.value() : unitId;
        List<Integer> unitIds = unitService.getSubUnit(unitId);
        return sysUserService.getUseByUnitId(unitIds);
    }
}
