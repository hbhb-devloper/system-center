package com.hbhb.cw.systemcenter.service;

import com.hbhb.api.core.bean.SelectVO;
import com.hbhb.cw.systemcenter.model.SysUser;
import com.hbhb.cw.systemcenter.vo.UserInfo;
import com.hbhb.cw.systemcenter.vo.UserReqVO;
import com.hbhb.cw.systemcenter.vo.UserResVO;
import org.beetl.sql.core.page.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @author xiaokang
 * @since 2020-10-06
 */
public interface SysUserService {

    /**
     * 按条件查询用户列表（分页）
     */
    PageResult<UserResVO> getUserPageByCond(Integer pageNum, Integer pageSize, UserReqVO cond);

    Map<Integer, String> getUseByUnitId(List<Integer> unitIdList);

    /**
     * 查询所有用户map
     */
    List<SelectVO> getAllUserMap(Integer unitId);

    /**
     * 按id查询用户详情
     */
    SysUser getUserById(Integer userId);

    /**
     * 添加用户
     */
    void addUser(SysUser user);

    /**
     * 更新用户（包含权限）
     */
    void updateUser(SysUser user);

    /**
     * 更新用户信息
     */
    void updateUserInfo(SysUser user);

    /**
     * 更新用户状态
     */
    void changeUserState(Integer userId, Byte state);

    /**
     * 修改用户密码
     */
    void updateUserPwd(Integer userId, String oldPwd, String newPwd);

    /**
     * 根据id获取用户信息
     */
    UserInfo getUserInfoById(Integer userId);

    /**
     * 根据登录名获取用户信息
     */
    UserInfo getUserInfoByName(String username);

    /**
     * 根据id批量查询获取用户信息
     */
    List<UserInfo> getUserInfoList(List<Integer> userIds);

    /**
     * 查询用户map(id-用户姓名)
     */
    Map<Integer, String> getUserMapById(List<Integer> userIds);

    /**
     * 跟据用户id获取获取用户签名图片
     *
     * @param userIds id
     * @return 签名图片map
     */
    Map<Integer, String> getUserSignature(List<Integer> userIds);
}
