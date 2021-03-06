package com.hbhb.cw.systemcenter.web.controller;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.core.bean.BeanConverter;
import com.hbhb.cw.systemcenter.api.UserApi;
import com.hbhb.cw.systemcenter.enums.ResourceType;
import com.hbhb.cw.systemcenter.enums.RoleType;
import com.hbhb.cw.systemcenter.enums.UnitEnum;
import com.hbhb.cw.systemcenter.mapper.SysUserUnitHallMapper;
import com.hbhb.cw.systemcenter.model.Hall;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.model.SysUserUnitHall;
import com.hbhb.cw.systemcenter.model.Unit;
import com.hbhb.cw.systemcenter.service.HallService;
import com.hbhb.cw.systemcenter.service.SysResourceService;
import com.hbhb.cw.systemcenter.service.SysRoleService;
import com.hbhb.cw.systemcenter.service.SysUserService;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.vo.RouterVO;
import com.hbhb.cw.systemcenter.vo.UserBasicsVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.vo.UserInfoVO;
import com.hbhb.cw.systemcenter.vo.UserPasswordVO;
import com.hbhb.cw.systemcenter.vo.UserReqVO;
import com.hbhb.cw.systemcenter.vo.UserResVO;
import com.hbhb.cw.systemcenter.web.vo.UserDetailVO;
import com.hbhb.cw.systemcenter.web.vo.UserPwdVO;
import com.hbhb.web.annotation.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.page.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Tag(name = "??????")
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
    private SysUserUnitHallMapper sysUserUnitHallMapper;
    @Resource
    private HallService hallService;

    @Operation(summary = "????????????????????????????????????????????????")
    @GetMapping("/list")
    public PageResult<UserResVO> pageUserByCond(
            @Parameter(description = "??????") @RequestParam(required = false) Integer pageNum,
            @Parameter(description = "????????????") @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "??????id") @RequestParam(required = false) Integer unitId,
            @Parameter(description = "?????????") @RequestParam(required = false) String userName,
            @Parameter(description = "?????????") @RequestParam(required = false) String nickName,
            @Parameter(description = "?????????") @RequestParam(required = false) String phone,
            @Parameter(description = "?????????0-?????????1-?????????") @RequestParam(required = false) Byte state) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        unitId = unitId == null ? UnitEnum.HANGZHOU.value() : unitId;
        List<Integer> unitIds = unitService.getSubUnit(unitId);
        List<Integer> hallIds = hallService.getSubHall(unitIds);
        unitIds.addAll(hallIds);
        return sysUserService.getUserPageByCond(
                pageNum, pageSize, UserReqVO.builder()
                        .unitIds(unitIds)
                        .userName(userName)
                        .nickName(nickName)
                        .phone(phone)
                        .state(state)
                        .build());
    }

    @Operation(summary = "??????????????????map", description = "??????id : ????????????-????????????")
    @GetMapping("/select")
    public List<SelectVO> getAllUserMap(@Parameter(description = "??????id") @RequestParam(required = false) Integer unitId) {
        return sysUserService.getAllUserMap(unitId);
    }

    @Operation(summary = "?????????id??????????????????", description = "?????????????????????????????????")
    @GetMapping("/{userId}")
    public SysUser getUserInfo(
            @Parameter(description = "??????id", required = true) @PathVariable Integer userId) {
        SysUser user = sysUserService.getUserById(userId);
        List<Integer> checkedRsRoleIds = sysRoleService.getCheckedRoleByUser(userId, RoleType.RELATE_RESOURCE.value());
        List<Integer> checkedUnRoleIds = sysRoleService.getCheckedRoleByUser(userId, RoleType.RELATE_UNIT.value());
        List<Integer> checkedUintIds = sysUserUnitHallMapper.
                createLambdaQuery().andEq(SysUserUnitHall::getUserId, userId)
                .select()
                .stream()
                .map(SysUserUnitHall::getUnitId).distinct()
                .collect(Collectors.toList());
        log.info("???????????????id{}", checkedUintIds);
        user.setCheckedRsRoleIds(checkedRsRoleIds);
        user.setCheckedUnRoleIds(checkedUnRoleIds);
        user.setCheckedUintIds(checkedUintIds);
        return user;
    }

    @Operation(summary = "????????????", description = "?????????????????????????????????")
    @PostMapping("")
    public void addUser(@Parameter(description = "????????????", required = true) @RequestBody SysUser user) {
        sysUserService.addUser(user);
    }

    @Operation(summary = "????????????", description = "?????????????????????????????????")
    @PutMapping("")
    public void updateUser(@Parameter(description = "????????????", required = true) @RequestBody SysUser user) {
        sysUserService.updateUser(user);
    }

    @Operation(summary = "??????????????????")
    @PutMapping("/{userId}/state")
    public void changeUserState(
            @Parameter(description = "??????id", required = true) @PathVariable Integer userId,
            @Parameter(description = "????????????0-?????????1-?????????", required = true) @RequestParam Byte state) {
        sysUserService.changeUserState(userId, state);
    }

    @Operation(summary = "??????????????????", description = "??????????????????")
    @PutMapping("/pwd")
    public void updateUserPwd(@Parameter(required = true) @RequestBody UserPwdVO vo,
                              @Parameter(hidden = true) @UserId Integer userId) {
        sysUserService.updateUserPwd(userId, vo.getOldPwd(), vo.getNewPwd());
    }

    @Operation(summary = "????????????-??????????????????")
    @GetMapping("/center-info")
    public UserDetailVO getUserCenterInfo(@Parameter(hidden = true) @UserId Integer userId) {
        SysUser user = sysUserService.getUserById(userId);
        return BeanConverter.convert(user, UserDetailVO.class);
    }

    @Operation(summary = "????????????-??????????????????")
    @PutMapping("/center-info")
    public void updateUserCenterInfo(
            @Parameter(description = "????????????", required = true) @RequestBody UserDetailVO vo) {
        SysUser user = new SysUser();
        BeanConverter.copyProp(vo, user);
        sysUserService.updateUserInfo(user);
    }

    @Operation(summary = "???????????????????????????????????????????????????????????????????????????")
    @GetMapping("/current")
    public UserInfoVO getCurrentUser(@Parameter(hidden = true) @UserId Integer userId) {
        UserInfo userInfo = sysUserService.getUserInfoById(userId);
        Unit unit = unitService.getUnitInfo(userInfo.getUnitId());
        Hall hallInfo = hallService.getHallInfo(userInfo.getUnitId());
        UserBasicsVO user = UserBasicsVO.builder()
                .defaultUnitId(userInfo.getDefaultUnitId())
                .id(userInfo.getId())
                .nickName(userInfo.getNickName())
                .userName(userInfo.getUserName())
                .unitName(!isEmpty(unit) ? unit.getUnitName() : hallInfo.getHallName())
                .build();
        // ?????????????????????????????????
        Set<String> permissions = sysResourceService.getUserPermission(
                userId, Collections.singletonList(ResourceType.BUTTON.value()));
        // ?????????????????????????????????
        List<RouterVO> navRouters = sysResourceService.getNavMenuTreeByUserId(userId);
        List<RouterVO> sideRouters = sysResourceService.getSideMenuTreeByUserId(userId);
        return UserInfoVO.builder()
                .userInfo(user)
                .permissions(permissions)
                .navRouters(navRouters)
                .sideRouters(sideRouters)
                .build();
    }

    @Operation(summary = "?????????id???????????????????????????")
    @Override
    public UserInfo getUserInfoById(@Parameter(description = "??????id", required = true) Integer userId) {
        return sysUserService.getUserInfoById(userId);
    }

    @Operation(summary = "???????????????????????????????????????")
    @Override
    public UserInfo getUserInfoByName(@Parameter(description = "?????????", required = true) String userName) {
        return sysUserService.getUserInfoByName(userName);
    }

    @Operation(summary = "???id??????????????????")
    @Override
    public List<UserInfo> getUserInfoBatch(
            @Parameter(description = "??????id??????", required = true) List<Integer> userIds) {
        return sysUserService.getUserInfoList(userIds);
    }

    @Operation(summary = "?????????????????????map", description = "??????id-????????????")
    @Override
    public Map<Integer, String> getUserMapById(
            @Parameter(description = "??????id?????????????????????????????????") List<Integer> userIds) {
        return sysUserService.getUserMapById(userIds);
    }

    @Operation(summary = "????????????????????????")
    @Override
    public List<Integer> getUserRoles(@Parameter(description = "??????id", required = true) Integer userId) {
        return sysRoleService.getRolesByUserId(userId);
    }

    @Operation(summary = "??????????????????????????????")
    @Override
    public Boolean isAdmin(@Parameter(description = "??????id", required = true) Integer userId) {
        return sysRoleService.isAdminRole(userId);
    }

    @Operation(summary = "??????????????????????????????????????????")
    @Override
    public Set<String> getUserPermission(
            @Parameter(description = "??????id", required = true) Integer userId,
            @Parameter(description = "????????????", required = true) List<String> types) {
        return sysResourceService.getUserPermission(userId, types);
    }

    @Operation(summary = "????????????????????????map???userId => userName???")
    @Override
    public Map<Integer, String> getUserByUnitIds(
            @Parameter(description = "??????id") @RequestParam(required = false) Integer unitId) {
        unitId = unitId == null ? UnitEnum.HANGZHOU.value() : unitId;
        List<Integer> unitIds = unitService.getSubUnit(unitId);
        return sysUserService.getUseByUnitId(unitIds);
    }

    @Operation(summary = "????????????????????????")
    @Override
    public Map<Integer, String> getUserSignature(
            @Parameter(description = "??????id?????????????????????????????????") List<Integer> userIds) {
        return sysUserService.getUserSignature(userIds);
    }


    @Operation(summary = "????????????????????????")
    @Override
    public void updatePwd(@Parameter(required = true) @RequestBody UserPasswordVO vo) {
        sysUserService.updatePwd(vo.getEmail(), vo.getNewPwd());
    }

    @Operation(summary = "??????????????????????????????")
    @Override
    public UserInfo getUserInfoByEmail(@Parameter(required = true) String email) {
        return sysUserService.getEmail(email);
    }
}
