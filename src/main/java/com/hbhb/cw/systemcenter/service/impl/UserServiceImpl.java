package com.hbhb.cw.systemcenter.service.impl;

import com.hbhb.core.bean.BeanConverter;
import com.hbhb.core.utils.AESCryptUtil;
import com.hbhb.cw.systemcenter.enums.code.UserErrorCode;
import com.hbhb.cw.systemcenter.exception.UserException;
import com.hbhb.cw.systemcenter.mapper.UserMapper;
import com.hbhb.cw.systemcenter.mapper.UserRoleMapper;
import com.hbhb.cw.systemcenter.model.User;
import com.hbhb.cw.systemcenter.model.UserRole;
import com.hbhb.cw.systemcenter.service.UserService;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.web.vo.UserReqVO;
import com.hbhb.cw.systemcenter.web.vo.UserResVO;

import org.beetl.sql.core.page.DefaultPageRequest;
import org.beetl.sql.core.page.PageRequest;
import org.beetl.sql.core.page.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Value("${cw.user.default-pwd}")
    private String defaultPwd;

    @Override
    @SuppressWarnings(value = {"rawtypes"})
    public PageResult<UserResVO> getUserPageByCond(Integer pageNum, Integer pageSize, UserReqVO cond) {
        PageRequest request = DefaultPageRequest.of(pageNum, pageSize);
        return userMapper.selectPageByCond(cond, request);
    }

    @Override
    public User getUserById(Integer userId) {
        return userMapper.single(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(User user) {
        long existUserName = userMapper.createLambdaQuery()
                .andEq(User::getUserName, user.getUserName())
                .count();
        if (existUserName > 0) {
            throw new UserException(UserErrorCode.REPEAT_OF_USER_NAME);
        }
        long existNickName = userMapper.createLambdaQuery()
                .andEq(User::getNickName, user.getNickName())
                .count();
        if (existNickName > 0) {
            throw new UserException(UserErrorCode.REPEAT_OF_NICK_NAME);
        }
        // 先用AES解密，再用BCrypt加密
        String plaintext = AESCryptUtil.decrypt(user.getPwd());
        // 添加时，如果密码明文为空，则使用默认密码
        if (StringUtils.isEmpty(plaintext)) {
            plaintext = defaultPwd;
        }
        // 进行bCrypt加密处理
        user.setPwd(new BCryptPasswordEncoder().encode(plaintext));
        // 先添加主表
        userMapper.insertTemplate(user);
        // 再添加关联数据
        insertUserRole(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) {
        // 先用AES解密，再用BCrypt加密
        String plaintext = AESCryptUtil.decrypt(user.getPwd());
        // 修改时，如果密码明文为空，则不做任何处理
        if (StringUtils.isEmpty(plaintext)) {
            user.setPwd(null);
        } else {
            // 进行bCrypt加密处理
            user.setPwd(new BCryptPasswordEncoder().encode(plaintext));
        }
        // 先更新主表
        userMapper.updateTemplateById(user);
        // 再添加关联数据
        insertUserRole(user);
    }

    @Override
    public void updateUserInfo(User user) {
        userMapper.updateTemplateById(user);
    }

    @Override
    public void changeUserState(Integer userId, Byte state) {
        userMapper.updateTemplateById(User.builder()
                .id(userId)
                .state(state)
                .build());
    }

    @Override
    public void updateUserPwd(Integer userId, String oldPwd, String newPwd) {
        User user = userMapper.single(userId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String oldPlaintext = AESCryptUtil.decrypt(oldPwd);
        String newPlaintext = AESCryptUtil.decrypt(newPwd);
        if (!encoder.matches(oldPlaintext, user.getPwd())) {
            throw new UserException(UserErrorCode.ORIGIN_PASSWORD_WRONG);
        }
        userMapper.updateTemplateById(User.builder()
                .id(userId)
                .pwd(encoder.encode(newPlaintext))
                .build());
    }

    @Override
    public UserInfo getUserInfoById(Integer userId) {
        User user = userMapper.single(userId);
        return BeanConverter.convert(user, UserInfo.class);
    }

    @Override
    public UserInfo getUserInfoByName(String username) {
        User user = userMapper.createLambdaQuery()
                .andEq(User::getUserName, username)
                .single();
        return BeanConverter.convert(user, UserInfo.class);

    }

    @Override
    public List<UserInfo> getUserInfoList(List<Integer> userIds) {
        List<User> users = userMapper.selectByIds(userIds);
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

    /**
     * 添加用户角色关联表
     */
    private void insertUserRole(User user) {
        // 先删除
        userRoleMapper.createLambdaQuery()
                .andEq(UserRole::getUserId, user.getId())
                .delete();

        // 再添加
        List<Integer> rsRoleIds = user.getCheckedRsRoleIds();
        List<Integer> unRoleIds = user.getCheckedUnRoleIds();
        if (rsRoleIds != null && unRoleIds != null) {
            List<Integer> roleIds = new ArrayList<>();
            roleIds.addAll(rsRoleIds);
            roleIds.addAll(unRoleIds);
            if (StringUtils.isEmpty(roleIds)) {
                return;
            }
            List<UserRole> list = new ArrayList<>();
            for (Integer roleId : roleIds) {
                list.add(UserRole.builder()
                        .userId(user.getId())
                        .roleId(roleId)
                        .build());
            }
            if (!CollectionUtils.isEmpty(list)) {
                userRoleMapper.insertBatch(list);
            }
        }
    }
}
