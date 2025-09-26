package com.example.dg.controller;

import com.example.dg.common.constant.Constants;
import com.example.dg.common.core.domain.entry.User;
import com.example.dg.common.core.domain.model.LoginBody;
import com.example.dg.result.Result;
import com.example.dg.common.core.domain.model.LoginUser;
import com.example.dg.service.common.LoginService;
import com.example.dg.service.impl.TokenService;
import com.example.dg.common.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@Tag(name = "登录模块")
@RestController
public class LoginController
{
    @Resource
    private LoginService loginService;

    @Resource
    private TokenService tokenService;


    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    @Operation(summary = "登录")
    public Result login(@RequestBody LoginBody loginBody)
    {
        Result result = Result.success();

        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), null,null);
        result.put(Constants.TOKEN, token);
        return result;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @Operation(summary = "获取用户信息")
    @GetMapping("getInfo")
    public Result getInfo(@RequestHeader("Authorization") String token)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        User user = loginUser.getUser();

        Result result = Result.success();
        result.put("user", user);
        return result;
    }

//    /**
//     * 获取路由信息
//     *
//     * @return 路由信息
//     */
//    @GetMapping("getRouters")
//    public AjaxResult getRouters()
//    {
//        Long userId = SecurityUtils.getUserId();
//        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
//        return AjaxResult.success(menuService.buildMenus(menus));
//    }
    
//    // 检查初始密码是否提醒修改
//    public boolean initPasswordIsModify(Date pwdUpdateDate)
//    {
//        Integer initPasswordModify = Convert.toInt(configService.selectConfigByKey("sys.account.initPasswordModify"));
//        return initPasswordModify != null && initPasswordModify == 1 && pwdUpdateDate == null;
//    }

    // 检查密码是否过期
//    public boolean passwordIsExpiration(Date pwdUpdateDate)
//    {
//        Integer passwordValidateDays = Convert.toInt(configService.selectConfigByKey("sys.account.passwordValidateDays"));
//        if (passwordValidateDays != null && passwordValidateDays > 0)
//        {
//            if (StringUtils.isNull(pwdUpdateDate))
//            {
//                // 如果从未修改过初始密码，直接提醒过期
//                return true;
//            }
//            Date nowDate = DateUtils.getNowDate();
//            return DateUtils.differentDaysByMillisecond(nowDate, pwdUpdateDate) > passwordValidateDays;
//        }
//        return false;
//    }
}
