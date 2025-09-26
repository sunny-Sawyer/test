package com.example.dg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dg.common.core.domain.entry.User;
import com.example.dg.result.Result;

import java.util.List;

/**
 * @author xinxin
 * @version 1.0
 */
public interface UserService extends IService<User> {
    /**
     * 导入用户数据
     *
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importUser(List<User> userList, Boolean isUpdateSupport, String operName);

//    Result logout();

//    Result login(User user);

//    Result center();

//    Result updatePassword(String oldPassword, String newPassword, String confirmPassword);

//    Result collect(Integer itemId);

//    Result uploadAvatar(MultipartFile avatar) throws IOException;

    //发送验证码
//    Result verifyAuthCode(String telephone, String authCode);

    //忘记密码
//    Result forgetPassword(String newPassword, String confirmPassword, String phone);

    //注册
//    Result register(User user, String confirmPwd, String authCode, Errors errors);

//    Result getAllUser(Integer pageNum, Integer pageSize);

//    Result deleteUser(Integer userId);

//    Result updateUser(User user, MultipartFile photo);

//    Result searchUserByUsernameOrPhone(Integer pageNum, Integer pageSize,String condition);

//    Result addUser(User user, MultipartFile photo);

//    Result addAdmin(User user, MultipartFile photo);

    public User selectUserByUserName(String userName);

    public User selectUserByPhone(String phone);

    /**
     * 分页查询用户列表
     *
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param condition 搜索条件（用户名或手机号）
     * @return 用户列表
     */
    public Result getAllUsers(Integer pageNum, Integer pageSize, String condition);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    public Result deleteUser(Integer userId);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public Result updateUserInfo(User user);

    /**
     * 设置用户管理员权限
     *
     * @param userId 用户ID
     * @param roleId 角色ID (1=村级管理员, 0=普通用户)
     * @return 结果
     */
    public Result setUserRole(Integer userId, String roleId);

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 结果
     */
    public Result addUser(User user);

    /**
     * 清除用户登录缓存，强制用户重新登录
     *
     * @param userId 用户ID
     * @return 结果
     */
    public Result clearUserLoginCache(Integer userId);
}
