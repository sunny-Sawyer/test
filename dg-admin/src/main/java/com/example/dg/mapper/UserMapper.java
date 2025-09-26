package com.example.dg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dg.common.core.domain.entry.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xinxin
 * @version 1.0
 */
//@Mapper
public interface UserMapper extends BaseMapper<User> {
    User selectUserById(Integer id);

    int insertUser(User user);

    int updateUserpasswordByphone(String newPwd, String phone);

    List<User> getAllUsers();

    User selectUserByNameOrPhoneAndPwd(String nameOrPhone, String password);

    User selectUserByNameOrPhone(String username, String phone);

    int updateUser(User user);

    int deleteUserById(Integer userId);

    public User selectUserByPhone(String phone);

    public User selectUserByUserName(String userName);

    /**
     * 根据条件分页查询用户列表
     *
     * @param page 分页参数
     * @param condition 搜索条件
     * @return 用户列表
     */
    Page<User> selectUserPageByCondition(Page<User> page, @Param("condition") String condition);

    /**
     * 更新用户角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 影响行数
     */
    int updateUserRole(Integer userId, String roleId);
}
