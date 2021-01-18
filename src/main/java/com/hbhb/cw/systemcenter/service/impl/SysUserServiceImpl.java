package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.core.bean.BeanConverter;
import com.hbhb.core.utils.AESCryptUtil;
import com.hbhb.core.utils.RegexUtil;
import com.hbhb.cw.systemcenter.enums.UserConstant;
import com.hbhb.cw.systemcenter.enums.code.UserErrorCode;
import com.hbhb.cw.systemcenter.exception.UserException;
import com.hbhb.cw.systemcenter.mapper.SysRoleUnitMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserRoleMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserUintHallMapper;
import com.hbhb.cw.systemcenter.model.SysRoleUnit;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.model.SysUserRole;
import com.hbhb.cw.systemcenter.model.SysUserUintHall;
import com.hbhb.cw.systemcenter.service.SysUserService;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.vo.CheckBoxVO;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.vo.UserReqVO;
import com.hbhb.cw.systemcenter.vo.UserResVO;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Service
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private SysRoleUnitMapper sysRoleUnitMapper;
    @Resource
    private UnitService unitService;
    @Resource
    private SysUserUintHallMapper userUintHallMapper;


    @Override
    public PageResult<UserResVO> getUserPageByCond(Integer pageNum, Integer pageSize, UserReqVO cond) {
        PageRequest request = DefaultPageRequest.of(pageNum, pageSize);
        return sysUserMapper.selectPageByCond(cond, request);
    }

    @Override
    public List<SelectVO> getAllUserMap() {
        Map<Integer, String> unitMap = unitService.getUnitMapById();
        List<SysUser> userList = sysUserMapper.createLambdaQuery()
                .select(SysUser::getId, SysUser::getNickName, SysUser::getUnitId);
        return userList.stream().map(user ->
                SelectVO.builder()
                        .id(Long.valueOf(user.getId()))
                        .label(user.getNickName() + "-" + unitMap.get(user.getUnitId()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public SysUser getUserById(Integer userId) {
        return sysUserMapper.single(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(SysUser user) {
        // 校验默认数据单位。默认数据单位必须在单位权限范围内
        if (!checkDefaultUnit(user)) {
            throw new UserException(UserErrorCode.DEFAULT_UNIT_SET_ERROR);
        }

        // 校验登录账号
        long existUserName = sysUserMapper.createLambdaQuery()
                .andEq(SysUser::getUserName, user.getUserName())
                .count();
        if (existUserName > 0) {
            throw new UserException(UserErrorCode.REPEAT_OF_USER_NAME);
        }

        // 校验用户姓名
        long existNickName = sysUserMapper.createLambdaQuery()
                .andEq(SysUser::getNickName, user.getNickName())
                .count();
        if (existNickName > 0) {
            throw new UserException(UserErrorCode.REPEAT_OF_NICK_NAME);
        }

        // 先用AES解密，再用BCrypt加密
        String plaintext = AESCryptUtil.decrypt(user.getPwd());
        // 如果密码明文为空，则使用默认密码
        if (StringUtils.isEmpty(plaintext)) {
            plaintext = UserConstant.DEFAULT_PASSWORD.value();
        }
        // 校验密码强度
        if (!RegexUtil.checkPwd(plaintext)) {
            throw new UserException(UserErrorCode.INCOMPATIBLE_PASSWORD);
        }

        // 进行bCrypt加密处理
        user.setPwd(new BCryptPasswordEncoder().encode(plaintext));
        // 先添加主表
        sysUserMapper.insertTemplate(user);
        // 再添加关联数据
        insertUserRole(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUser user) {
        // 默认数据单位必须在单位权限范围内
        if (!checkDefaultUnit(user)) {
            throw new UserException(UserErrorCode.DEFAULT_UNIT_SET_ERROR);
        }

        // 先用AES解密，再用BCrypt加密
        String plaintext = AESCryptUtil.decrypt(user.getPwd());

        // 修改时，如果密码明文为空，则不做任何处理
        if (StringUtils.isEmpty(user.getPwd())) {
            user.setPwd(null);
        } else {
            // 校验密码强度
            if (!RegexUtil.checkPwd(plaintext)) {
                throw new UserException(UserErrorCode.INCOMPATIBLE_PASSWORD);
            }
            // 进行bCrypt加密处理
            user.setPwd(new BCryptPasswordEncoder().encode(plaintext));
        }
        // 先更新主表
        sysUserMapper.updateTemplateById(user);
        // 再添加关联数据
        insertUserRole(user);
    }

    @Override
    public void updateUserInfo(SysUser user) {
        sysUserMapper.updateTemplateById(user);
    }

    @Override
    public void changeUserState(Integer userId, Byte state) {
        sysUserMapper.updateTemplateById(SysUser.builder()
                .id(userId)
                .state(state)
                .build());
    }

    @Override
    public void updateUserPwd(Integer userId, String oldPwd, String newPwd) {
        SysUser user = sysUserMapper.single(userId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String oldPlaintext = AESCryptUtil.decrypt(oldPwd);
        String newPlaintext = AESCryptUtil.decrypt(newPwd);
        // 校验密码强度
        if (!RegexUtil.checkPwd(newPlaintext)) {
            throw new UserException(UserErrorCode.INCOMPATIBLE_PASSWORD);
        }
        // 校验原密码是否正确
        if (!encoder.matches(oldPlaintext, user.getPwd())) {
            throw new UserException(UserErrorCode.ORIGIN_PASSWORD_WRONG);
        }
        sysUserMapper.updateTemplateById(SysUser.builder()
                .id(userId)
                .pwd(encoder.encode(newPlaintext))
                .build());
    }

    @Override
    public UserInfo getUserInfoById(Integer userId) {
        SysUser user = sysUserMapper.single(userId);
        return BeanConverter.convert(user, UserInfo.class);
    }

    @Override
    public UserInfo getUserInfoByName(String username) {
        SysUser user = sysUserMapper.createLambdaQuery()
                .andEq(SysUser::getUserName, username)
                .single();
        return user == null ? new UserInfo() : BeanConverter.convert(user, UserInfo.class);

    }

    @Override
    public List<UserInfo> getUserInfoList(List<Integer> userIds) {
        List<SysUser> users = sysUserMapper.selectByIds(userIds);
        return Optional.ofNullable(users)
                .orElse(new ArrayList<>())
                .stream()
                .map(user -> UserInfo.builder()
                        .email(user.getEmail())
                        .nickName(user.getNickName())
                        .id(user.getId())
                        .unitId(user.getUnitId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, String> getUserMapById(List<Integer> userIds) {
        List<SysUser> users;
        if (CollectionUtils.isEmpty(userIds)) {
            users = sysUserMapper.createLambdaQuery()
                    .select(SysUser::getId, SysUser::getNickName);
        } else {
            users = sysUserMapper.createLambdaQuery()
                    .andIn(SysUser::getId, userIds)
                    .select(SysUser::getId, SysUser::getNickName);
        }
        return Optional.ofNullable(users)
                .orElse(new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(SysUser::getId, SysUser::getNickName));
    }

    /**
     * 校验默认数据单位是否在设定的单位权限范围内
     */
    private boolean checkDefaultUnit(SysUser user) {
        // 用户的默认单位
        Integer defaultUnitId = user.getDefaultUnitId();
        // 勾选的单位权限
        List<Integer> roleIds = user.getCheckedUnRoleIds();
        if (defaultUnitId == null || CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
//        List<SysRoleUnit> list = sysRoleUnitMapper.createLambdaQuery()
//                .andIn(SysRoleUnit::getRoleId, roles)
//                .select();
//        // 单位权限中包含的单位id
//        List<Integer> unitIds = list.stream().map(SysRoleUnit::getUnitId).collect(Collectors.toList());
        return roleIds.contains(defaultUnitId);
    }

    /**
     * 添加用户角色关联表
     */
    private void insertUserRole(SysUser user) {
        // 先删除
        sysUserRoleMapper.createLambdaQuery()
                .andEq(SysUserRole::getUserId, user.getId())
                .delete();

        userUintHallMapper.createLambdaQuery()
                .andEq(SysUserUintHall::getUserId,user.getId())
                .delete();

        // 再添加
        List<Integer> rsRoleIds = user.getCheckedRsRoleIds();
        List<Integer> unRoleIds = user.getCheckedUnRoleIds();
//        List<Integer> unRoleIds = userUintHallMapper
//                .createLambdaQuery()
//                .andEq(SysUserUintHall::getUserId,user.getId())
//                .select()
//                .stream()
//                .map(SysUserUintHall::getUintId)
//                .collect(Collectors.toList())
//                .stream().distinct()
//                .collect(Collectors.toList());

        if (rsRoleIds != null && unRoleIds != null) {
            List<Integer> roleIds = new ArrayList<>();
            roleIds.addAll(rsRoleIds);
            roleIds.addAll(unRoleIds);
            if (StringUtils.isEmpty(roleIds)) {
                return;
            }

             List<SysUserRole> list = roleIds
                     .stream()
                     .map(id -> SysUserRole.builder().roleId(id).userId(user.getId()).build())
                     .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());

            List<SysUserUintHall> userUintHalls = unRoleIds
                    .stream()
                    .map(id -> SysUserUintHall.builder().uintId(id).userId(user.getId()).build())
                    .collect(Collectors.toList());
//            List<SysUserRole> list = new ArrayList<>();
//            for (Integer roleId : roleIds) {
//                list.add(SysUserRole.builder()
//                        .userId(user.getId())
//                        .roleId(roleId)
//                        .build());
//            }
            if (!CollectionUtils.isEmpty(userUintHalls) && !CollectionUtils.isEmpty(list)) {
                userUintHallMapper.insertBatch(userUintHalls);
                sysUserRoleMapper.insertBatch(list);
            }
        }
    }
}
