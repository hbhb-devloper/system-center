package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.core.bean.BeanConverter;
import com.hbhb.core.utils.AESCryptUtil;
import com.hbhb.core.utils.RegexUtil;
import com.hbhb.cw.systemcenter.enums.UnitEnum;
import com.hbhb.cw.systemcenter.enums.UserConstant;
import com.hbhb.cw.systemcenter.enums.code.UserErrorCode;
import com.hbhb.cw.systemcenter.exception.UserException;
import com.hbhb.cw.systemcenter.mapper.SysUserMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserRoleMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserSignatureMapper;
import com.hbhb.cw.systemcenter.mapper.SysUserUnitHallMapper;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.model.SysUserRole;
import com.hbhb.cw.systemcenter.model.SysUserSignature;
import com.hbhb.cw.systemcenter.model.SysUserUnitHall;
import com.hbhb.cw.systemcenter.service.HallService;
import com.hbhb.cw.systemcenter.service.SysUserService;
import com.hbhb.cw.systemcenter.service.UnitService;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.vo.UserReqVO;
import com.hbhb.cw.systemcenter.vo.UserResVO;
import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.beetl.sql.core.query.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

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
    private UnitService unitService;
    @Resource
    private SysUserUnitHallMapper userUnitHallMapper;
    @Resource
    private SysUserSignatureMapper signatureMapper;
    @Resource
    private HallService hallService;


    @Override
    public PageResult<UserResVO> getUserPageByCond(Integer pageNum, Integer pageSize, UserReqVO cond) {
        PageRequest request = DefaultPageRequest.of(pageNum, pageSize);
        PageResult<UserResVO> user = sysUserMapper.selectPageByCond(cond, request);
        Map<Integer, String> unitMap = unitService.getUnitMapById();
        Map<Integer, String> hallMap = hallService.getUnitMapById();
        user.getList().forEach(item -> {
            item.setUnitName(isEmpty(unitMap.get(item.getUnitId())) ?
                    hallMap.get(item.getUnitId()) :
                    unitMap.get(item.getUnitId()));
        });
        return user;
    }

    @Override
    public Map<Integer, String> getUseByUnitId(List<Integer> unitIdList) {
        List<SysUser> sysUserList = sysUserMapper.createLambdaQuery()
                .andIn(SysUser::getUnitId, unitIdList).select();
        return sysUserList.stream().collect(Collectors.toMap(SysUser::getId, SysUser::getNickName));
    }

    @Override
    public List<SelectVO> getAllUserMap(Integer unitId) {
        if (UnitEnum.BENBU.value().equals(unitId) || UnitEnum.HANGZHOU.value().equals(unitId)) {
            unitId = null;
        }
        Map<Integer, String> unitMap = unitService.getUnitMapById();
        List<SysUser> userList = sysUserMapper.createLambdaQuery()
                .andEq(SysUser::getUnitId, Query.filterEmpty(unitId))
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
        // ???????????????????????????????????????????????????????????????????????????
        if (!checkDefaultUnit(user)) {
            throw new UserException(UserErrorCode.DEFAULT_UNIT_SET_ERROR);
        }

        // ??????????????????
        long existUserName = sysUserMapper.createLambdaQuery()
                .andEq(SysUser::getUserName, user.getUserName())
                .count();
        if (existUserName > 0) {
            throw new UserException(UserErrorCode.REPEAT_OF_USER_NAME);
        }

        // ??????????????????
        long existNickName = sysUserMapper.createLambdaQuery()
                .andEq(SysUser::getNickName, user.getNickName())
                .count();
        if (existNickName > 0) {
            throw new UserException(UserErrorCode.REPEAT_OF_NICK_NAME);
        }

        // ??????AES???????????????BCrypt??????
        String plaintext = AESCryptUtil.decrypt(user.getPwd());
        // ????????????????????????????????????????????????
        if (StringUtils.isEmpty(plaintext)) {
            plaintext = UserConstant.DEFAULT_PASSWORD.value();
        }
        // ??????????????????
        if (!RegexUtil.checkPwd(plaintext)) {
            throw new UserException(UserErrorCode.INCOMPATIBLE_PASSWORD);
        }

        // ??????bCrypt????????????
        user.setPwd(new BCryptPasswordEncoder().encode(plaintext));
        // ???????????????
        sysUserMapper.insertTemplate(user);
        // ?????????????????????
        insertUserRole(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUser user) {
        // ????????????????????????????????????????????????
        if (!checkDefaultUnit(user)) {
            throw new UserException(UserErrorCode.DEFAULT_UNIT_SET_ERROR);
        }

        // ??????AES???????????????BCrypt??????
        String plaintext = AESCryptUtil.decrypt(user.getPwd());

        // ????????????????????????????????????????????????????????????
        if (StringUtils.isEmpty(user.getPwd())) {
            user.setPwd(null);
        } else {
            // ??????????????????
            if (!RegexUtil.checkPwd(plaintext)) {
                throw new UserException(UserErrorCode.INCOMPATIBLE_PASSWORD);
            }
            // ??????bCrypt????????????
            user.setPwd(new BCryptPasswordEncoder().encode(plaintext));
        }
        // ???????????????
        sysUserMapper.updateTemplateById(user);
        // ?????????????????????
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
        // ??????????????????
        if (!RegexUtil.checkPwd(newPlaintext)) {
            throw new UserException(UserErrorCode.INCOMPATIBLE_PASSWORD);
        }
        // ???????????????????????????
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

    @Override
    public Map<Integer, String> getUserSignature(List<Integer> userIds) {
        List<SysUserSignature> signature = signatureMapper
                .createLambdaQuery()
                .andIn(SysUserSignature::getUserId, Query.filterNull(userIds))
                .select();
        return signature.stream()
                .collect(Collectors
                        .toMap(SysUserSignature::getUserId, SysUserSignature::getImagePath));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePwd(String email, String newPwd) {
        // ????????????????????????
        SysUser user = sysUserMapper.createLambdaQuery().andEq(SysUser::getEmail, email).single();
        if (isEmpty(user)) {
            throw new UserException(UserErrorCode.EMAIL_INPUT_ERROR);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPlaintext = AESCryptUtil.decrypt(newPwd);
        // ??????????????????
        if (!RegexUtil.checkPwd(newPlaintext)) {
            throw new UserException(UserErrorCode.INCOMPATIBLE_PASSWORD);
        }
        sysUserMapper.updateTemplateById(SysUser.builder()
                .id(user.getId())
                .pwd(encoder.encode(newPlaintext))
                .build());
    }

    @Override
    public UserInfo getEmail(String email) {
        // ????????????????????????
        SysUser user = sysUserMapper.createLambdaQuery().andEq(SysUser::getEmail, email).single();
        if (isEmpty(user)) {
            throw new UserException(UserErrorCode.EMAIL_INPUT_ERROR);
        }
        UserInfo u = new UserInfo();
        BeanUtils.copyProperties(user, u);
        return u;
    }

    /**
     * ???????????????????????????????????????????????????????????????
     */
    private boolean checkDefaultUnit(SysUser user) {
        // ?????????????????????
        Integer defaultUnitId = user.getDefaultUnitId();
        // ?????????????????????
        List<Integer> roleIds = user.getCheckedUnRoleIds();
        if (defaultUnitId == null || CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
//        List<SysRoleUnit> list = sysRoleUnitMapper.createLambdaQuery()
//                .andIn(SysRoleUnit::getRoleId, roles)
//                .select();
//        // ??????????????????????????????id
//        List<Integer> unitIds = list.stream().map(SysRoleUnit::getUnitId).collect(Collectors.toList());
        return roleIds.contains(defaultUnitId);
    }

    /**
     * ???????????????????????????
     */
    private void insertUserRole(SysUser user) {


        // ?????????
        sysUserRoleMapper.createLambdaQuery()
                .andEq(SysUserRole::getUserId, user.getId())
                .delete();

//        userUnitHallMapper.createLambdaQuery()
//                .andEq(SysUserUnitHall::getUserId, user.getId())
//                .delete();

        //?????????????????????????????????????????????
        List<SysUserUnitHall> hallIds = userUnitHallMapper.createLambdaQuery()
                .andEq(SysUserUnitHall::getUserId, user.getId())
                .select()
                .stream()
                .filter(sysUserUintHall -> sysUserUintHall.getHallId() != null && sysUserUintHall.getHallId() != 0)
                .distinct()
                .collect(Collectors.toList());


        List<SysUserUnitHall> userUintHalls = new ArrayList<>();
        if (!hallIds.isEmpty()) {
            if (user.getCheckedUnRoleIds().isEmpty()) {
                return;
            }
            user.getCheckedUnRoleIds().forEach(unitId -> hallIds.forEach(sysUserUintHall -> {
                if (unitId.equals(sysUserUintHall.getUnitId())) {
                    userUintHalls.add(sysUserUintHall);
                } else {
                    //???????????????????????????????????????????????????????????????
                    userUintHalls.add(SysUserUnitHall.builder().unitId(unitId).userId(user.getId()).build());
                }
            }));

        } else {
            if (user.getCheckedUnRoleIds().isEmpty()) {
                return;
            }
            //??????????????????????????????????????????????????????
            user.getCheckedUnRoleIds().forEach(unitId -> userUintHalls.add(SysUserUnitHall.builder().unitId(unitId).userId(user.getId()).build()));

        }

        //?????????????????????id??????????????????
        userUnitHallMapper.createLambdaQuery()
                .andEq(SysUserUnitHall::getUserId, user.getId())
                .delete();

        // ?????????
        List<Integer> rsRoleIds = user.getCheckedRsRoleIds();
        List<Integer> unRoleIds = user.getCheckedUnRoleIds();

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

//            List<SysUserUnitHall> userUintHalls = unRoleIds
//                    .stream()
//                    .map(id -> SysUserUnitHall.builder().unitId(id).userId(user.getId()).build())
//                    .collect(Collectors.toList());
//            List<SysUserRole> list = new ArrayList<>();
//            for (Integer roleId : roleIds) {
//                list.add(SysUserRole.builder()
//                        .userId(user.getId())
//                        .roleId(roleId)
//                        .build());
//            }
            if (!CollectionUtils.isEmpty(list)) {
                userUnitHallMapper.insertBatch(userUintHalls);
                sysUserRoleMapper.insertBatch(list);
            }
        }
    }
}
