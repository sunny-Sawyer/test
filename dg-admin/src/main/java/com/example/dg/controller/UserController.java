package com.example.dg.controller;


import com.example.dg.common.core.domain.entry.User;
import com.example.dg.common.utils.poi.ExcelUtil;
import com.example.dg.common.core.redis.RedisCache;
import com.example.dg.result.Result;
import com.example.dg.service.UserService;
import com.example.dg.service.common.VerificationCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.example.dg.common.utils.SecurityUtils.getUsername;
import static com.example.dg.result.Result.success;

/**
 * @author xinxin
 * @version 1.0
 */
@Tag(name = "用户模块")
@Controller
@ResponseBody
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private VerificationCodeService verificationCodeService;
//    @Resource
//    private FavoritesService favoritesService;//收藏夹服务

    @Operation(summary = "导入用户数据")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    @PostMapping("/importData")
    public Result importData(@RequestHeader("Authorization") String token,
                            MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        List<User> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "导入用户数据的模版")
    @PostMapping("/importTemplate")
    public void importTemplate(@RequestHeader("Authorization") String token,HttpServletResponse response)
    {
        ExcelUtil<User> util = new ExcelUtil<User>(User.class);
        util.importTemplateExcel(response, "用户数据");
    }

    @Operation(summary = "分页查询用户列表")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    @PostMapping("/admin/user/list")
    public Result getUserList(@RequestHeader("Authorization") String token,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam(required = false) String condition) {
        return userService.getAllUsers(pageNum, pageSize, condition);
    }

    @Operation(summary = "删除用户")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    @PostMapping("/admin/user/delete")
    public Result deleteUser(@RequestHeader("Authorization") String token,
                            @RequestParam Integer userId) {
        return userService.deleteUser(userId);
    }

    @Operation(summary = "修改用户信息")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    @PostMapping("/admin/user/update")
    public Result updateUser(@RequestHeader("Authorization") String token,
                            @RequestBody User user) {
        return userService.updateUserInfo(user);
    }

    @Operation(summary = "设置用户角色")
    @PreAuthorize("@ss.hasRoleId('2')")
    @PostMapping("/admin/user/setRole")
    public Result setUserRole(@RequestHeader("Authorization") String token,
                             @RequestParam Integer userId, 
                             @RequestParam String roleId) {
        return userService.setUserRole(userId, roleId);
    }

    @Operation(summary = "添加用户")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    @PostMapping("/admin/user/add")
    public Result addUser(@RequestHeader("Authorization") String token,
                         @RequestBody User user) {
        return userService.addUser(user);
    }

    @Operation(summary = "清除用户登录缓存")
    @PreAuthorize("@ss.hasRoleId('1,2')")
    @PostMapping("/admin/user/clearCache")
    public Result clearUserLoginCache(@RequestHeader("Authorization") String token,
                                     @RequestParam Integer userId) {
        return userService.clearUserLoginCache(userId);
    }

//    @GetMapping("/member/getFavorites")
//    @Operation(summary = "获取收藏夹")
//    public Result getFavorites(@RequestHeader("token") String token) {
//        return favoritesService.getFavorites();
//    }

//    @PostMapping("/member/collect")
//    @Operation(summary = "收藏")
//    public Result collect(@RequestHeader("token") String token,
//                          @RequestParam Integer itemId) {
//        return userService.collect(itemId);
//    }

//    @GetMapping("/member/login")
//    @Operation(summary = "登录")
//    public Result login(@RequestBody @Parameter(name = "user") User user) {
//        return userService.login(user);
//    }
//
//    @GetMapping("/member/logout")
//    @Operation(summary = "退出登录")
//    public Result logout(@RequestHeader("token") String token) {
//        return userService.logout();
//    }
//
//    @PostMapping("/member/register")
//    @Operation(summary = "注册")
//    public Result register(@Validated @RequestBody User user,
//                           @RequestParam String confirmPwd,
//                           @RequestParam String authCode,
//                           Errors errors) {
//        return userService.register(user, confirmPwd, authCode, errors);
//    }
//
//    @GetMapping("/sms/sendcode")
//    @Operation(summary = "发送验证码")
//    public Result sendCode(@RequestParam("phone") String phone) throws Exception {
//        verificationCodeService.sendVerificationCode(phone);
//        return Result.success("验证码发送成功");
//    }
//
//    @PostMapping("/verifyAuthCode")
//    @Operation(summary = "验证验证码")
//    public Result verifyAuthCode(@RequestParam String telephone,
//                                 @RequestParam String authCode) {
//        return userService.verifyAuthCode(telephone, authCode);
//    }
//
//    @PutMapping("/member/forgetPassword")
//    @Operation(summary = "忘记密码")
//    public Result forgetPassword(String newPassword, String confirmPassword, String phone) {
//        return userService.forgetPassword(newPassword, confirmPassword, phone);
//    }
//
//    @PutMapping("/member/uploadAvatar")
//    @Operation(summary = "上传头像")
//    public Result uploadAvatar(@RequestParam("avatar") MultipartFile avatar,
//                               @RequestHeader("token") String token) throws IOException {
//        return userService.uploadAvatar(avatar);
//    }
//
//    @GetMapping("/member/center")
//    @Operation(summary = "个人中心")
//    public Result center(@RequestHeader("token") String token) {
//        return userService.center();
//    }
//
//    @PutMapping("/member/updatePwd")
//    @Operation(summary = "修改密码")
//    public Result updatePwd(@RequestParam("oldPwd") String oldPwd,
//                            @RequestParam("newPwd") String newPwd,
//                            @RequestParam("confirmPwd") String confirmPwd,
//                            @RequestHeader("token") String token) {
//        return userService.updatePassword(oldPwd, newPwd, confirmPwd);
//    }
//
//    @GetMapping("/admin/user/all")
//    @Operation(summary = "查看所有用户")
//    public Result getAllUser(@RequestHeader("token") String token,
//                             @RequestParam(defaultValue = "1") Integer pageNum,
//                             @RequestParam(defaultValue = "5") Integer pageSize) {
//        return userService.getAllUser(pageNum, pageSize);
//    }
//
////    @DeleteMapping("/admin/user/delete")
////    @Operation(summary = "删除用户")
////    public Result deleteUser(@RequestHeader("token") String token,
////                             @RequestParam Integer userId) {
////        return userService.deleteUser(userId);
////    }
//
//    @PutMapping("/admin/user/update")
//    @Operation(summary = "修改用户信息")
//    public Result updateUser(@RequestHeader("token") String token,
//                             @RequestParam(value = "photo", required = false) MultipartFile photo,
//                             @RequestBody User user) {
//        return userService.updateUser(user, photo);
//    }
//
//    @GetMapping("/admin/user/search")
//    @Operation(summary = "搜索用户")
//    public Result searchUserByUsernameOrPhone(@RequestHeader("token") String token,
//                                              @RequestParam(defaultValue = "1") Integer pageNum,
//                                              @RequestParam(defaultValue = "5") Integer pageSize,
//                                              @RequestParam(required = false) String condition) {
//        return userService.searchUserByUsernameOrPhone(pageNum, pageSize, condition);
//    }
//
//    @PostMapping("/admin/user/add")
//    @Operation(summary = "添加用户")
//    public Result addUser(@RequestHeader("token") String token,
//                          @RequestParam(value = "photo", required = false) MultipartFile photo,
//                          @RequestBody User user) {
//        user.setRole(0);
//        return userService.addUser(user, photo);
//    }
//    @PostMapping("/admin/user/addAdmin")
//    @Operation(summary = "添加管理员")
//    public Result addAdmin(@RequestHeader("token") String token,
//                           @RequestParam(value = "photo", required = false) MultipartFile photo,
//                           @RequestBody User user) {
//        user.setRole(1);
//        return userService.addAdmin(user, photo);
//    }
}
