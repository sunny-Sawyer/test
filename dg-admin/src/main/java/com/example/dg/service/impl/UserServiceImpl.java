package com.example.dg.service.impl;//package com.example.dg.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.example.dg.common.core.domain.entry.User;
//import com.example.dg.mapper.UserMapper;
//import com.example.dg.result.Result;
//import com.example.dg.common.core.domain.model.LoginUser;
//import com.example.dg.redis.RedisCache;
//import com.example.dg.service.UserService;
//import com.example.dg.service.common.VerificationCodeService;
//import com.example.dg.utils.*;
//import com.example.dg.pojo.vo.UserVO;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.validation.Errors;
//import org.springframework.validation.FieldError;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.annotation.Resource;
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dg.common.constant.CacheConstants;
import com.example.dg.common.core.domain.model.LoginUser;
import com.example.dg.common.exception.ServiceException;
import com.example.dg.common.utils.SecurityUtils;
import com.example.dg.common.utils.StringUtils;
import com.example.dg.common.utils.bean.BeanValidators;
import com.example.dg.mapper.UserMapper;
import com.example.dg.common.core.domain.entry.User;
import com.example.dg.service.UserService;
import com.example.dg.result.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dg.common.core.redis.RedisCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.Collection;
import java.util.List;

/**
 * @author xinxin
 * @version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    protected Validator validator;

    @Resource
    private RedisCache redisCache;

//    @Autowired
//    private ISysConfigService configService;
//    @Resource
//    private RedisCache redisCache;
//    @Resource
//    private AuthenticationManager authenticationManager;
//    @Resource
//    private VerificationCodeService verificationCodeService;
////    @Resource
////    private FavoritesService favoritesService;//收藏夹服务
////    @Resource
////    private FavoritesMapper favoritesMapper;
//    @Resource
//    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
//    @Value("${upload.avatar-dir}")
//    private String uploadAvatarDir;


    /**
     * 导入用户数据
     *
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<User> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (User user : userList)
        {
            try
            {
                // 验证是否存在这个用户
                User u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u))
                {
                    BeanValidators.validateWithException(validator, user);
//                    deptService.checkDeptDataScope(user.getDeptId());
//                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    String password = "123456";
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, user);
//                    checkUserAllowed(u);
//                    checkUserDataScope(u.getUserId());
//                    deptService.checkDeptDataScope(user.getDeptId());
                    user.setUserId(u.getUserId());
                    user.setVillageId(u.getVillageId());
                    user.setUpdateBy(operName);
                    userMapper.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
//    @Override
//    public Result updateUser(User user, MultipartFile photo) {
//        String newFileName = null;
//        if (photo != null) {
////        String coverImagePath = null;
////            try {
////                coverImagePath = UploadUtil.getLocalPath(photo, WebUtils2.getUploadAvatarDirectory());
////            } catch (IOException e) {
////                return Result.error("400", "图片上传失败");
////            }
//            // 生成新文件名（防止重复）
//            String originalFilename = photo.getOriginalFilename();
//            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            newFileName = UUID.randomUUID() + fileExtension;
//            // 保存文件到统一目录
//            File targetFile = new File(uploadAvatarDir + newFileName);
//            //avatar.transferTo(targetFile);
//            try {
//                if (!photo.isEmpty()) {
//                    photo.transferTo(targetFile);
//                    System.out.println("文件已保存至: " + targetFile.getAbsolutePath());
//                } else {
//                    System.out.println("上传文件为空！");
//                }
//            } catch (IOException e) {
//                System.out.println("上传失败");
//                return Result.error("400", "上传失败");
//            }
//            user.setAvatar("/avatar/" + newFileName);
//        }
//        try {
//            userMapper.updateUser(user);
//        } catch (Exception e) {
//            return Result.error("400", "修改失败");
//        }
//        return Result.success("200", "修改成功");
//    }
//
//    @Override
//    public Result searchUserByUsernameOrPhone(Integer pageNum, Integer pageSize, String condition) {
//        UsernamePasswordAuthenticationToken authentication =
//                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        User user = loginUser.getUser();
//        QueryWrapper<User> queryWrapper = Wrappers.query();
//        if (StringUtils.hasText(condition)) {
//            queryWrapper.like("username", condition);
//            queryWrapper.or();
//            queryWrapper.like("phone", condition);
//        }
//        if (user.getRole() == 1) {
//            queryWrapper.and(i -> i.eq("role", 0));
//        }
//        if (user.getRole() == 2) {
//            queryWrapper.and(i -> i.eq("role", 0).or().eq("role", 1));
//        }
//        Page<User> page = page(new Page<>(pageNum, pageSize), queryWrapper);
//        List<User> users = page.getRecords();
//        ArrayList<UserVO> userVOS = new ArrayList<>();
//        setVOS(userVOS, users);
//        Result success = Result.success("200", "查询成功");
//        success.add("list", userVOS);
//        return success;
//    }
//
//    @Override
//    public Result addUser(User user, MultipartFile photo) {
//        String newFileName = null;
//        if (photo != null) {
////        String coverImagePath = null;
////            try {
////                coverImagePath = UploadUtil.getLocalPath(photo, WebUtils2.getUploadAvatarDirectory());
////            } catch (IOException e) {
////                return Result.error("400", "图片上传失败");
////            }
//            // 生成新文件名（防止重复）
//            String originalFilename = photo.getOriginalFilename();
//            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            newFileName = UUID.randomUUID() + fileExtension;
//            // 保存文件到统一目录
//            File targetFile = new File(uploadAvatarDir + newFileName);
//            //avatar.transferTo(targetFile);
//            try {
//                if (!photo.isEmpty()) {
//                    photo.transferTo(targetFile);
//                    System.out.println("文件已保存至: " + targetFile.getAbsolutePath());
//                } else {
//                    System.out.println("上传文件为空！");
//                }
//            } catch (IOException e) {
//                System.out.println("上传失败");
//                return Result.error("400", "上传失败");
//            }
//            user.setAvatar("/avatar/" + newFileName);
//        }
//        user.setRole(0);
//        try {
//            userMapper.insert(user);
//        } catch (Exception e) {
//            return Result.error("400", "添加失败");
//        }
//        return Result.success("200", "添加成功");
//    }
//
//    @Override
//    public Result addAdmin(User user, MultipartFile photo) {
//        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        User superUser = loginUser.getUser();
//        if (superUser.getRole() != 2) {
//            return Result.error("400", "权限不足");
//        }
//        String newFileName = null;
//        if (photo != null) {
//            // 生成新文件名（防止重复）
//            String originalFilename = photo.getOriginalFilename();
//            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            newFileName = UUID.randomUUID() + fileExtension;
//            // 保存文件到统一目录
//            File targetFile = new File(uploadAvatarDir + newFileName);
//            //avatar.transferTo(targetFile);
//            try {
//                if (!photo.isEmpty()) {
//                    photo.transferTo(targetFile);
//                    System.out.println("文件已保存至: " + targetFile.getAbsolutePath());
//                } else {
//                    System.out.println("上传文件为空！");
//                }
//            } catch (IOException e) {
//                System.out.println("上传失败");
//                return Result.error("400", "上传失败");
//            }
//            user.setAvatar("/avatar/" + newFileName);
//        }
//        try {
//            userMapper.insert(user);
//        } catch (Exception e) {
//            return Result.error("400", "添加失败");
//        }
//        return Result.success("200", "添加成功");
//    }
//
    /**
     *通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户信息
     */
    @Override
    public User selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    @Override
    public User selectUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public Result getAllUsers(Integer pageNum, Integer pageSize, String condition) {
        try {
            // 使用MyBatis-Plus分页插件在数据库层面分页
            Page<User> page = new Page<>(pageNum, pageSize);
            Page<User> resultPage = userMapper.selectUserPageByCondition(page, condition);
            
            Result result = Result.success("查询成功");
            result.put("list", resultPage.getRecords());
            result.put("total", resultPage.getTotal());
            result.put("pageNum", pageNum);
            result.put("pageSize", pageSize);
            result.put("pages", resultPage.getPages());
            return result;
        } catch (Exception e) {
            log.error("查询用户列表失败", e);
            return Result.error("查询用户列表失败");
        }
    }

    @Override
    public Result deleteUser(Integer userId) {
        if (userId == null || userId <= 0) {
            return Result.error("用户ID不能为空");
        }
        try {
            User user = userMapper.selectUserById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 软删除，设置删除标志
            user.setDelFlag("2");
            user.setUpdateBy(SecurityUtils.getUsername());
            int result = userMapper.updateUser(user);
            
            if (result > 0) {
                return Result.success("删除用户成功");
            } else {
                return Result.error("删除用户失败");
            }
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return Result.error("删除用户失败");
        }
    }

    @Override
    public Result updateUserInfo(User user) {
        if (user.getUserId() == null) {
            return Result.error("用户ID不能为空");
        }
        try {
            User existUser = userMapper.selectUserById(user.getUserId().intValue());
            if (existUser == null) {
                return Result.error("用户不存在");
            }
            
            // 如果包含密码修改，需要进行加密
            if (StringUtils.isNotEmpty(user.getPassword())) {
                user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
                user.setPwdUpdateDate(new java.util.Date());
            }

            // 如果包含密码修改，需要进行加密
            if (StringUtils.isNotEmpty(user.getPassword())) {
                user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
                user.setPwdUpdateDate(new java.util.Date());
            }
            
            // 验证用户数据
            BeanValidators.validateWithException(validator, user);
            
            user.setUpdateBy(SecurityUtils.getUsername());
            int result = userMapper.updateUser(user);
            
            if (result > 0) {
                return Result.success("修改用户信息成功");
            } else {
                return Result.error("修改用户信息失败");
            }
        } catch (Exception e) {
            log.error("修改用户信息失败", e);
            return Result.error("修改用户信息失败：" + e.getMessage());
        }
    }

    @Override
    public Result setUserRole(Integer userId, String roleId) {
        if (userId == null || userId <= 0) {
            return Result.error("用户ID不能为空");
        }
        if (StringUtils.isEmpty(roleId)) {
            return Result.error("角色ID不能为空");
        }
        
        // 验证roleId只能为0、1、2
        if (!"0".equals(roleId) && !"1".equals(roleId) && !"2".equals(roleId)) {
            return Result.error("角色ID只能为0（普通用户）、1（村级管理员）、2（超级管理员）");
        }
        
        try {
            User user = userMapper.selectUserById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            int result = userMapper.updateUserRole(userId, roleId);
            
            if (result > 0) {
                // 清除该用户的登录缓存，强制重新登录以获取最新权限
                try {
                    clearUserLoginTokens(userId);
                    System.out.println("用户" + userId + "角色更新成功，已清除登录缓存");
                } catch (Exception e) {
                    System.out.println("清除用户登录缓存失败: " + e.getMessage());
                }
                
                String roleName = "普通用户";
                if ("1".equals(roleId)) {
                    roleName = "村级管理员";
                } else if ("2".equals(roleId)) {
                    roleName = "超级管理员";
                } else if ("0".equals(roleId)) {
                    roleName = "普通用户";
                }
                return Result.success("设置用户角色成功，已设置为" + roleName + "，请让用户重新登录以生效最新权限");
            } else {
                return Result.error("设置用户角色失败");
            }
        } catch (Exception e) {
            log.error("设置用户角色失败", e);
            return Result.error("设置用户角色失败");
        }
    }

    @Override
    public Result addUser(User user) {
        try {
            // 验证必要字段
            if (StringUtils.isEmpty(user.getUserName())) {
                return Result.error("用户名不能为空");
            }
            if (StringUtils.isEmpty(user.getPassword())) {
                return Result.error("密码不能为空");
            }
            
            // 检查用户名是否已存在
            User existUser = userMapper.selectUserByUserName(user.getUserName());
            if (existUser != null) {
                return Result.error("用户名已存在");
            }
            
            // 检查手机号是否已存在（如果提供了手机号）
            if (StringUtils.isNotEmpty(user.getPhone())) {
                User existUserByPhone = userMapper.selectUserByPhone(user.getPhone());
                if (existUserByPhone != null) {
                    return Result.error("手机号已存在");
                }
            }
            
            // 验证角色ID
            if (StringUtils.isNotEmpty(user.getRoleId())) {
                if (!"0".equals(user.getRoleId()) && !"1".equals(user.getRoleId()) && !"2".equals(user.getRoleId())) {
                    return Result.error("角色ID只能为0（普通用户）、1（村级管理员）、2（超级管理员）");
                }
            } else {
                // 默认设置为普通用户
                user.setRoleId("0");
            }
            
            // 密码加密
            user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
            
            // 设置默认值
            user.setStatus("0"); // 正常状态
            user.setDelFlag("0"); // 未删除
            user.setCreateBy(SecurityUtils.getUsername());
            
            // 数据验证
            BeanValidators.validateWithException(validator, user);
            
            int result = userMapper.insertUser(user);
            
            if (result > 0) {
                return Result.success("添加用户成功");
            } else {
                return Result.error("添加用户失败");
            }
        } catch (Exception e) {
            log.error("添加用户失败", e);
            return Result.error("添加用户失败：" + e.getMessage());
        }
    }

    @Override
    public Result clearUserLoginCache(Integer userId) {
        if (userId == null || userId <= 0) {
            return Result.error("用户ID不能为空");
        }
        try {
            clearUserLoginTokens(userId);
            return Result.success("清除用户登录缓存成功，请让用户重新登录");
        } catch (Exception e) {
            return Result.error("清除用户登录缓存失败");
        }
    }

    /**
     * 清除指定用户的所有登录token缓存
     * 
     * @param userId 用户ID
     */
    private void clearUserLoginTokens(Integer userId) {
        try {
            // 获取所有登录token的keys
            Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
            
            for (String key : keys) {
                try {
                    // 获取缓存中的LoginUser对象
                    LoginUser loginUser = redisCache.getCacheObject(key);
                    if (loginUser != null && loginUser.getUser() != null 
                        && userId.equals(loginUser.getUser().getUserId().intValue())) {
                        // 删除该用户的登录缓存
                        redisCache.deleteObject(key);
                        System.out.println("已清除用户" + userId + "的登录token: " + key);
                    }
                } catch (Exception e) {
                    // 忽略单个key的处理错误，继续处理其他key
                    System.out.println("处理key " + key + " 时出错: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("清除用户登录缓存失败: " + e.getMessage());
            throw e;
        }
    }
//
//    @Override
//    public Result logout() {
//        //获取SecurityContextHolder中的用户id
//        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        Integer userid = loginUser.getUser().getUserId();
//        //删除redis中的值
//        redisCache.deleteObject("login:" + userid);
//        return Result.success("注销成功");
//    }
//
//
//    public Result login(User user) {
//        if (user.getUsername() == null) {
//            user.setUsername(userMapper.selectUserByPhone(user.getPhone()).getUsername());
//        }
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        if (Objects.isNull(authenticate)) {
//            throw new RuntimeException("登录失败");
//        }
//        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
//
//        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
//        String userid = loginUser.getUser().getUserId().toString();
//
//        HashMap<String, Object> claims = new HashMap<>();
//        claims.put("userId", userid);
//        String jwt = TokenUtil.createJWT(claims);
//
//        //把完整的用户信息存入redis  userid作为key
//        redisCache.setCacheObject("login:" + userid, loginUser);
//        Result success = Result.success("200", "登录成功");
//        success.add("token", jwt);
//        return success;
//    }
//
//    @Override
//    public Result center() {
//        UsernamePasswordAuthenticationToken authentication =
//                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        Integer userId = loginUser.getUser().getUserId();
////        Claims claims = TokenUtil.parseJWT(token);
////        Integer userId = (Integer) claims.get("userId");
//        if (userId == null) {
//            return Result.error("400", "还未登录");
//        }
//        User user = userMapper.selectUserById(userId);
//        if (user == null) {
//            return Result.error("400", "用户不存在");
//        }
//        UserVO userVO = new UserVO();
//        userVO.setUsername(user.getUsername());
//        userVO.setPhone(user.getPhone());
//        userVO.setAvatar(user.getAvatar());
//        String profile = user.getProfile();
//        if (profile == null) {
//            profile = "";
//        }
//        Integer role = user.getRole();
//        if (role == 1) {
//            userVO.setRole("管理员");
//        } else if (role == 0) {
//            userVO.setRole("普通用户");
//        } else if (role == 2) {
//            userVO.setRole("超级管理员");
//        } else {
//            userVO.setRole("游客");
//        }
//        Integer gender = user.getGender();
//        if (gender == null) {
//            userVO.setGender("未知");
//        } else if (gender == 1) {
//            userVO.setGender("男");
//        } else if (gender == 0) {
//            userVO.setGender("女");
//        }
//        userVO.setProfile(profile);
//        Result success = Result.success();
//        success.add("user", userVO);
//        return success;
//    }
//
//    @Override
//    public Result updatePassword(String oldPassword, String newPassword, String confirmPassword) {
//        UsernamePasswordAuthenticationToken authentication =
//                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        User user = loginUser.getUser();
//        Integer userId = user.getUserId();
//        if (userId == null) {
//            return Result.error("400", "登录失效，重新登录");
//        }
//        user = userMapper.selectUserById(userId);
//        String password = user.getPassword();
////        if(!PasswordEncoder.encryptWithMD5(oldPwd).equals(password)){
////            return Result.error("400", "旧密码错误");
////        }
//        if (!PasswordEncoder.matches(oldPassword, password)) {
//            return Result.error("400", "旧密码错误");
//        }
//        if (!newPassword.equals(confirmPassword)) {
//            return Result.error("400", "两次密码不一致");
//        }
//        if (oldPassword.equals(newPassword)) {
//            return Result.error("400", "新密码与旧密码相同");
//        }
//        // 加密
//        String EncryptedPwd = PasswordEncoder.encode(newPassword);
//        user.setPassword(EncryptedPwd);
////        user.setPassword(newPassword);
//        userMapper.updateUser(user);
//        return Result.success("200", "修改密码成功");
//    }
//
////    @Override
////    public Result collect(Integer itemId) {
////        if (itemId == null) {
////            return Result.error("400", "收藏失败");
////        }
////        UsernamePasswordAuthenticationToken authentication =
////                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
////        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
////        User user = loginUser.getUser();
////        Integer userId = user.getUserId();
////        if (userId == null) {
////            return Result.error("400", "登录失效，重新登录");
////        }
////        List<Integer> itemsByUserId = favoritesService.getItemsByUserId(userId);
////        if (itemsByUserId.contains(itemId)) {
////            return Result.error("400", "该项目已收藏");
////        }
////        int i = favoritesService.addItemToFavorites(userId, itemId);
////        if (i == 0) {
////            return Result.error("400", "收藏失败");
////        }
////        return Result.success();
////    }
//
//
//    @Override
//    public Result uploadAvatar(MultipartFile avatar) throws IOException {
//        UsernamePasswordAuthenticationToken authentication =
//                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        User user = userMapper.selectUserById(loginUser.getUser().getUserId());
//        if (user == null) {
//            return Result.error("400", "登录失效，重新登录");
//        }
//        if (StringUtils.hasText(user.getAvatar())) {
//            String oldFileName = user.getAvatar().replace("/avatar/", "");
//            String oldFilePath = uploadAvatarDir + oldFileName;
//            File oldFile = new File(oldFilePath);
//            if (oldFile.exists()) {
//                oldFile.delete();
//            }
//        }
//        File dir = new File(uploadAvatarDir);
//        if (!dir.exists()) {
//            dir.mkdirs(); // 递归创建目录
//        }
//        // 生成新文件名（防止重复）
//        String originalFilename = avatar.getOriginalFilename();
//        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String newFileName = UUID.randomUUID() + fileExtension;
//        // 保存文件到统一目录
//        File targetFile = new File(uploadAvatarDir + newFileName);
//        //avatar.transferTo(targetFile);
//        try {
//            System.out.println(avatar.getSize());
//            if (!avatar.isEmpty()) {
//                avatar.transferTo(targetFile);
//                System.out.println("文件已保存至: " + targetFile.getAbsolutePath());
//            } else {
//                System.out.println("上传文件为空！");
//            }
//        } catch (IOException e) {
//            System.out.println("上传失败");
//            return Result.error("400", "上传失败");
//        }
//        // 保存相对路径到数据库（如 /avatar/a1b2c3d4.jpg）
//        user.setAvatar("/avatar/" + newFileName);
//        userMapper.updateUser(user);
//
//        return Result.success("200", "上传成功");
//    }
//
//    @Override
//    public Result verifyAuthCode(String telephone, String authCode) {
//        // 从Redis中获取验证码
//        String realAuthCode = redisCache.getCacheObject("code:" + telephone);
//        boolean result = authCode.equals(realAuthCode);
//        if (result) {
//            Result success = Result.success();
//            success.setMsg("验证码" + realAuthCode + "正确");
//            return success;
//        } else {
//            return Result.error("400", "验证码错误");
//        }
//    }
//
//    @Override
//    public Result forgetPassword(String newPassword, String confirmPassword, String phone) {
//        User user = userMapper.selectUserByNameOrPhone(phone, phone);
//        if (user == null) {
//            return Result.error("400", "用户不存在,请先注册");
//        }
//        if (!newPassword.equals(confirmPassword)) {
//            return Result.error("400", "两次密码不一致");
//        }
//        //        传入新密码，进行加密
//        String realPassword = PasswordEncoder.encode(newPassword);
//        int result = userMapper.updateUserpasswordByphone(realPassword, phone);
//        if (result == 1) {
//            return Result.success("200", "密码修改成功");
//        } else {
//            return Result.error("400", "密码修改失败");
//        }
//    }
//
//    @Override
//    public Result register(User user, String confirmPwd, String authCode, Errors errors) {
//        HashMap<String, Object> data = new HashMap<>();
//        List<FieldError> fieldErrors = errors.getFieldErrors();
//        for (FieldError fieldError : fieldErrors) {
//            data.put(fieldError.getField(), fieldError.getDefaultMessage());
//        }
//        if (!data.isEmpty()) {
//            for (Map.Entry<String, Object> entry : data.entrySet()) {
//                log.error("错误信息=" + entry.getKey() + "---" + entry.getValue());
//            }
//            return Result.error("400", "验证失败", data);
//        }
//        if (!user.getPassword().equals(confirmPwd)) {
//            return Result.error("400", "两次密码不一致");
//        }
//        String realAuthCode = redisCache.getCacheObject("code:" + user.getPhone());
//        if (realAuthCode == null) {
//            return Result.error("400", "验证码已过期");
//        }
//
//        if (!authCode.equals(realAuthCode)) {
//            return Result.error("400", "验证码不正确");
//        }
//        if (userMapper.selectUserByNameOrPhone(user.getUsername(), user.getPhone()) != null) {
//            return Result.error("400", "用户名或手机号已存在");
//        }
//        user.setPassword(PasswordEncoder.encode(user.getPassword()));
//        userMapper.insertUser(user);
//        Integer userId = userMapper.selectUserByNameOrPhone(user.getUsername(), user.getPhone()).getUserId();
//        HashMap<String, Object> claims = new HashMap<>();
//        claims.put("userId", userId);
//        String jwt = TokenUtil.createJWT(claims);
//        Result success = Result.success("200", "注册成功");
//        success.add("token", jwt);
//        return success;
//    }
//
//    @Override
//    public Result getAllUser(Integer pageNum, Integer pageSize) {
//        UsernamePasswordAuthenticationToken authentication =
//                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        User user = loginUser.getUser();
//        QueryWrapper<User> queryWrapper = Wrappers.query();
//        if (user.getRole() == 1) {
//            queryWrapper.eq("role", 0);
//        }
//        if (user.getRole() == 2) {
//            queryWrapper.eq("role", 0);
//            queryWrapper.or();
//            queryWrapper.eq("role", 1);
//        }
//        ArrayList<UserVO> userVOS = new ArrayList<>();
//        Page<User> page = page(new Page<>(pageNum, pageSize), queryWrapper);
//        List<User> users = page.getRecords();
//        setVOS(userVOS, users);
//        Result success = Result.success("200", "返回用户集合");
//        success.add("list", userVOS);
//        return success;
//    }
//
////    @Override
////    public Result deleteUser(Integer userId) {
////        favoritesMapper.deleteFavoritesByUserId(userId);
////        int i = userMapper.deleteUserById(userId);
////        if (i == 1) {
////            return Result.success("200", "删除成功");
////        } else {
////            return Result.error("400", "删除失败");
////        }
////    }
//
//    private void setVOS(ArrayList<UserVO> userVOS, List<User> users) {
//        for (User user : users) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//            UserVO userVO = new UserVO();
//            userVO.setId(user.getUserId());
//            userVO.setUsername(user.getUsername());
//            userVO.setPhone(user.getPhone());
//            userVO.setAvatar(user.getAvatar());
//            Integer gender = user.getGender();
//            if (gender == null) {
//                userVO.setGender("未知");
//            } else if (gender == 1) {
//                userVO.setGender("男");
//            } else if (gender == 0) {
//                userVO.setGender("女");
//            }
//            userVO.setProfile(user.getProfile());
//            Integer role = user.getRole();
//            if (role == 0) {
//                userVO.setRole("普通用户");
//            } else if (role == 1) {
//                userVO.setRole("管理员");
//            } else if (role == 2) {
//                userVO.setRole("超级管理员");
//            }
//            userVO.setProfile(user.getProfile());
//            userVOS.add(userVO);
//        }
//    }
//
//
}
