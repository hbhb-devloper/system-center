package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.core.bean.BeanConverter;
import com.hbhb.cw.systemcenter.api.UserApi;
import com.hbhb.cw.systemcenter.enums.ResourceType;
import com.hbhb.cw.systemcenter.enums.RoleType;
import com.hbhb.cw.systemcenter.model.User;
import com.hbhb.cw.systemcenter.service.ResourceService;
import com.hbhb.cw.systemcenter.service.RoleService;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.service.UserService;
import com.hbhb.cw.systemcenter.vo.RouterVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.vo.UserInfoVO;
import com.hbhb.cw.systemcenter.web.vo.UserDetailVO;
import com.hbhb.cw.systemcenter.web.vo.UserPwdVO;
import com.hbhb.cw.systemcenter.web.vo.UserReqVO;
import com.hbhb.cw.systemcenter.web.vo.UserResVO;
import com.hbhb.web.annotation.UserId;

import org.beetl.sql.core.page.PageResult;
import org.springframework.beans.factory.annotation.Value;
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
public class UserController implements UserApi {

    @Resource
    private UserService userService;
    @Resource
    private UnitService unitService;
    @Resource
    private RoleService roleService;
    @Resource
    private ResourceService resourceService;

    @Value("${cw.unit-id.hangzhou}")
    private Integer hangzhou;

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
        unitId = unitId == null ? hangzhou : unitId;
        List<Integer> unitIds = unitService.getSubUnitByDeep(unitId);
        return userService.getUserPageByCond(
                pageNum, pageSize, UserReqVO.builder()
                        .unitIds(unitIds)
                        .userName(userName)
                        .nickName(nickName)
                        .phone(phone)
                        .state(state)
                        .build());
    }

    @Operation(summary = "按用户id获取详细信息", description = "管理员查看用户详情页面")
    @GetMapping("/{userId}")
    public User getUserInfo(
            @Parameter(description = "用户id", required = true) @PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        List<Integer> checkedRsRoleIds = roleService.getCheckedRoleByUser(userId, RoleType.RELATE_RESOURCE.value());
        List<Integer> checkedUnRoleIds = roleService.getCheckedRoleByUser(userId, RoleType.RELATE_UNIT.value());
        user.setCheckedRsRoleIds(checkedRsRoleIds);
        user.setCheckedUnRoleIds(checkedUnRoleIds);
        return user;
    }

    @Operation(summary = "添加用户", description = "管理员新增用户信息页面")
    @PostMapping("")
    public void addUser(@Parameter(description = "用户信息", required = true) @RequestBody User user) {
        userService.addUser(user);
    }

    @Operation(summary = "修改用户", description = "管理员修改用户信息页面")
    @PutMapping("")
    public void updateUser(@Parameter(description = "用户信息", required = true) @RequestBody User user) {
        userService.updateUser(user);
    }

    @Operation(summary = "转换用户状态")
    @PutMapping("/{userId}/state")
    public void changeUserState(
            @Parameter(description = "用户id", required = true) @PathVariable Integer userId,
            @Parameter(description = "状态值（0-禁用、1-启用）", required = true) @RequestParam Byte state) {
        userService.changeUserState(userId, state);
    }

    @Operation(summary = "修改用户密码", description = "个人中心页面")
    @PutMapping("/pwd")
    public void updateUserPwd(@Parameter(required = true) @RequestBody UserPwdVO vo,
                              @Parameter(hidden = true) @UserId Integer userId) {
        userService.updateUserPwd(userId, vo.getOldPwd(), vo.getNewPwd());
    }

    @Operation(summary = "个人中心-获取用户详情")
    @GetMapping("/center-info")
    public UserDetailVO getUserCenterInfo(@Parameter(hidden = true) @UserId Integer userId) {
        User user = userService.getUserById(userId);
        return BeanConverter.convert(user, UserDetailVO.class);
    }

    @Operation(summary = "个人中心-修改用户信息")
    @PutMapping("/center-info")
    public void updateUserCenterInfo(
            @Parameter(description = "用户信息", required = true) @RequestBody UserDetailVO vo) {
        User user = new User();
        BeanConverter.copyProp(vo, user);
        userService.updateUserInfo(user);
    }

    @Operation(summary = "获取当前登录用户的信息（包括基本信息、资源权限等）")
    @GetMapping("/current")
    public UserInfoVO getCurrentUser(@Parameter(hidden = true) @UserId Integer userId) {
        UserInfo user = userService.getUserInfoById(userId);
        // 获取登录用户的按钮权限
        Set<String> permissions = resourceService.getUserPermission(
                userId, Collections.singletonList(ResourceType.BUTTON.value()));
        // 获取登录用户的菜单路由
        List<RouterVO> navRouters = resourceService.getNavMenuTreeByUserId(userId);
        List<RouterVO> sideRouters = resourceService.getSideMenuTreeByUserId(userId);
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
        return userService.getUserInfoById(userId);
    }

    @Operation(summary = "按登录名获取用户的基本信息")
    @Override
    public UserInfo getUserInfoByName(@Parameter(description = "登录名", required = true) String userName) {
        return userService.getUserInfoByName(userName);
    }

    @Operation(summary = "按id批量查询用户")
    @Override
    public List<UserInfo> getUserInfoBatch(
            @Parameter(description = "用户id列表", required = true) List<Integer> userIds) {
        return userService.getUserInfoList(userIds);
    }

    @Operation(summary = "获取用户map", description = "用户id-用户姓名")
    @Override
    public Map<Integer, String> getUserMapById() {
        return userService.getUserMapById();
    }

    @Operation(summary = "获取用户所有角色")
    @Override
    public List<Integer> getUserRoles(@Parameter(description = "用户id", required = true) Integer userId) {
        return roleService.getRolesByUserId(userId);
    }

    @Operation(summary = "校验用户是否为管理员")
    @Override
    public Boolean isAdmin(@Parameter(description = "用户id", required = true) Integer userId) {
        return roleService.isAdminRole(userId);
    }

    @Operation(summary = "获取用户的权限（按类型）")
    @Override
    public Set<String> getUserPermission(Integer userId, List<String> types) {
        return resourceService.getUserPermission(userId, types);
    }
}
